package com.assignment.catawiki.pokemon.species.data.species

import com.assignment.catawiki.common.runCatchingFromSuspend
import com.assignment.catawiki.pokemon.species.BuildConfig
import com.assignment.catawiki.pokemon.species.data.pagination.PokemonSpeciesFeedPaginationDataSource
import com.assignment.catawiki.pokemon.species.data.pagination.model.PaginationData
import com.assignment.catawiki.pokemon.species.data.species.local.PokemonSpeciesLocalDataSource
import com.assignment.catawiki.pokemon.species.data.species.local.model.SpeciesEntity
import com.assignment.catawiki.pokemon.species.data.species.local.model.UpdateSpeciesEvolution
import com.assignment.catawiki.pokemon.species.data.species.mapper.EvolutionChainDtoMapper
import com.assignment.catawiki.pokemon.species.data.species.mapper.PokemonSpeciesDetailsDtoMapper
import com.assignment.catawiki.pokemon.species.data.species.mapper.PokemonSpeciesDtoMapper
import com.assignment.catawiki.pokemon.species.data.species.mapper.SpeciesEntityMapper
import com.assignment.catawiki.pokemon.species.data.species.remote.PokemonSpeciesRemoteDataSource
import com.assignment.catawiki.pokemon.species.domain.PokemonSpeciesRepository
import com.assignment.catawiki.pokemon.species.domain.error.GetSpeciesDetailsError
import com.assignment.catawiki.pokemon.species.domain.error.GetSpeciesEvolutionError
import com.assignment.catawiki.pokemon.species.domain.error.GetSpeciesPageError
import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpecies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class PokemonSpeciesRepositoryImpl @Inject constructor(
    private val remoteDataSource: PokemonSpeciesRemoteDataSource,
    private val localDataSource: PokemonSpeciesLocalDataSource,
    private val feedPaginationDataSource: PokemonSpeciesFeedPaginationDataSource,
    private val pokemonSpeciesDtoMapper: PokemonSpeciesDtoMapper,
    private val pokemonSpeciesDetailsDtoMapper: PokemonSpeciesDetailsDtoMapper,
    private val evolutionChainDtoMapper: EvolutionChainDtoMapper,
    private val speciesEntityMapper: SpeciesEntityMapper,
) : PokemonSpeciesRepository {

    override fun getAllPokemonSpecies(): Flow<List<PokemonSpecies>> {
        return localDataSource.getAllSpecies()
            .map { species -> species.map(speciesEntityMapper::map) }
    }

    override fun getPokemonSpecies(id: Long): Flow<PokemonSpecies> {
        return localDataSource.getSpecies(id)
            .map(speciesEntityMapper::map)
    }

    override suspend fun getSpeciesDetails(id: Long): Result<Unit> {
        val storedSpecies = localDataSource.getSpecies(id).first()
        if (storedSpecies.description != null) return Result.success(Unit)

        val detailsResult = runCatchingFromSuspend {
            remoteDataSource.fetchPokemonDetails(id)
        }.map { detailsDto ->
            val updateSpeciesDetails = pokemonSpeciesDetailsDtoMapper.map(detailsDto)
            localDataSource.updateDetails(updateSpeciesDetails)
            updateSpeciesDetails
        }
            .onFailure {
                return Result.failure(GetSpeciesDetailsError())
            }

        if (detailsResult.isSuccess) {
            val details = detailsResult.getOrThrow()
            val evolutionChainUrl = requireNotNull(details.evolutionChainUrl)
            fetchEvolutionChain(evolutionChainUrl, storedSpecies.name)
                .onSuccess {
                    localDataSource.updateEvolution(UpdateSpeciesEvolution(id, it))
                }
                .onFailure { return Result.failure(GetSpeciesEvolutionError()) }
        }

        return Result.success(Unit)
    }

    override suspend fun getSpeciesEvolution(id: Long): Result<Unit> {
        val species = localDataSource.getSpecies(id).first()
        return fetchEvolutionChain(requireNotNull(species.evolutionChainUrl), species.name)
            .onSuccess { evolution ->
                localDataSource.updateEvolution(UpdateSpeciesEvolution(id, evolution))
            }.map { }
    }

    private suspend fun fetchEvolutionChain(
        url: String,
        forSpeciesName: String
    ): Result<SpeciesEntity.Evolution> {
        return runCatchingFromSuspend { remoteDataSource.fetchEvolutionChain(url) }
            .map { evolutionChainDto ->
                evolutionChainDtoMapper.map(evolutionChainDto, forSpeciesName)
            }
    }

    override suspend fun getNextSpeciesPage(refresh: Boolean): Result<Unit> {
        if (refresh) {
            feedPaginationDataSource.clearPaginationData()
        }
        val paginationData = feedPaginationDataSource.getPaginationData()
        val urlPath = if (paginationData?.next != null) {
            paginationData.next
        } else {
            BuildConfig.INITIAL_FEED_URL_PATH
        }
        return runCatchingFromSuspend {
            val paginationDto = remoteDataSource.fetchPokemonPage(urlPath)
            feedPaginationDataSource.savePaginationData(
                PaginationData(paginationDto.count, paginationDto.next)
            )
            val speciesEntities = paginationDto.results.map(pokemonSpeciesDtoMapper::map)

            if (refresh) {
                localDataSource.removeAll()
            }
            localDataSource.save(speciesEntities)
        }.onFailure {
            return Result.failure(GetSpeciesPageError())
        }
    }

    override suspend fun getStoredItemsCount(): Long {
        return localDataSource.getCount()
    }
}
