package com.yerastov.assignment.pokedex.core.pokemon.species.data.species

import com.yerastov.assignment.pokedex.core.common.runCatchingFromSuspend
import com.yerastov.assignment.pokedex.core.pokemon.species.BuildConfig
import com.yerastov.assignment.pokedex.core.pokemon.species.data.pagination.PokemonSpeciesFeedPaginationDataSource
import com.yerastov.assignment.pokedex.core.pokemon.species.data.pagination.model.PaginationData
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.PokemonSpeciesLocalDataSource
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.model.SpeciesEntity
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.model.UpdateCaptureRateDifference
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.model.UpdateSpeciesDetails
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.model.UpdateSpeciesEvolution
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.mapper.EvolutionChainDtoMapper
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.mapper.PokemonSpeciesDetailsDtoMapper
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.mapper.PokemonSpeciesDtoMapper
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.mapper.SpeciesEntityMapper
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.remote.PokemonSpeciesRemoteDataSource
import com.yerastov.assignment.pokedex.core.pokemon.species.domain.PokemonSpeciesRepository
import com.yerastov.assignment.pokedex.core.pokemon.species.domain.error.GetSpeciesDetailsError
import com.yerastov.assignment.pokedex.core.pokemon.species.domain.error.GetSpeciesEvolutionError
import com.yerastov.assignment.pokedex.core.pokemon.species.domain.error.GetSpeciesPageError
import com.yerastov.assignment.pokedex.core.pokemon.species.domain.model.PokemonSpecies
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

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
        if (storedSpecies.description != null && storedSpecies.evolution == null) {
            return runCatchingFromSuspend { getSpeciesEvolutionInternal(storedSpecies) }
                .onFailure { return Result.failure(GetSpeciesEvolutionError()) }
        } else if (storedSpecies.description != null) {
            return Result.success(Unit)
        }

        runCatchingFromSuspend { getSpeciesDetailsInternal(id) }
            .onFailure { return Result.failure(GetSpeciesDetailsError()) }

        val updatedSpecies = localDataSource.getSpecies(id).first()
        runCatchingFromSuspend { getSpeciesEvolutionInternal(updatedSpecies) }
            .onFailure { return Result.failure(GetSpeciesEvolutionError()) }

        return Result.success(Unit)
    }

    private suspend fun getSpeciesDetailsInternal(id: Long): UpdateSpeciesDetails {
        val detailsDto = remoteDataSource.fetchPokemonDetails(id)
        val updateSpeciesDetails = pokemonSpeciesDetailsDtoMapper.map(detailsDto)
        localDataSource.updateDetails(updateSpeciesDetails)
        return updateSpeciesDetails
    }

    override suspend fun getSpeciesEvolution(id: Long): Result<Unit> {
        val species = localDataSource.getSpecies(id).first()
        return runCatchingFromSuspend { getSpeciesEvolutionInternal(species) }
            .onFailure { return Result.failure(GetSpeciesEvolutionError()) }
    }

    private suspend fun getSpeciesEvolutionInternal(species: SpeciesEntity) {
        if (species.evolutionChainUrl == null) {
            localDataSource.updateEvolution(
                UpdateSpeciesEvolution(species.id, SpeciesEntity.Evolution.Final)
            )
        } else {
            val evolutionChainDto = remoteDataSource.fetchEvolutionChain(species.evolutionChainUrl)
            val speciesEvolution = evolutionChainDtoMapper.map(evolutionChainDto, species.name)
            if (speciesEvolution is SpeciesEntity.Evolution.EvolvesTo) {
                val evolutionLinkDetails = getSpeciesDetailsInternal(speciesEvolution.pokemonId)
                val captureRateDifference =
                    if (species.captureRate != null && evolutionLinkDetails.captureRate != null) {
                        species.captureRate - evolutionLinkDetails.captureRate
                    } else {
                        null
                    }

                captureRateDifference?.let {
                    localDataSource.updateCaptureRateDifference(
                        UpdateCaptureRateDifference(species.id, captureRateDifference)
                    )
                }
            }
            localDataSource.updateEvolution(UpdateSpeciesEvolution(species.id, speciesEvolution))
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
