package com.assignment.catawiki.pokemon.species.data.species

import com.assignment.catawiki.common.runCatchingFromSuspend
import com.assignment.catawiki.pokemon.species.BuildConfig
import com.assignment.catawiki.pokemon.species.data.pagination.PokemonSpeciesFeedPaginationDataSource
import com.assignment.catawiki.pokemon.species.data.pagination.model.PaginationData
import com.assignment.catawiki.pokemon.species.data.species.mapper.EvolutionChainDtoMapper
import com.assignment.catawiki.pokemon.species.data.species.mapper.PokemonSpeciesDetailsDtoMapper
import com.assignment.catawiki.pokemon.species.data.species.mapper.PokemonSpeciesFeedItemDtoMapper
import com.assignment.catawiki.pokemon.species.data.species.remote.PokemonSpeciesRemoteDataSource
import com.assignment.catawiki.pokemon.species.domain.PokemonSpeciesRepository
import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpecies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class PokemonSpeciesRepositoryImpl @Inject constructor(
    private val remoteDataSource: PokemonSpeciesRemoteDataSource,
    private val feedPaginationDataSource: PokemonSpeciesFeedPaginationDataSource,
    private val pokemonSpeciesFeedItemDtoMapper: PokemonSpeciesFeedItemDtoMapper,
    private val pokemonSpeciesDetailsDtoMapper: PokemonSpeciesDetailsDtoMapper,
    private val evolutionChainDtoMapper: EvolutionChainDtoMapper,
) : PokemonSpeciesRepository {

    private val inMemoryPokemonFeed = MutableStateFlow<List<PokemonSpecies>>(emptyList())

    override fun getAllPokemonSpecies(): Flow<List<PokemonSpecies>> {
        return inMemoryPokemonFeed
    }

    override fun getPokemonSpecies(id: Long): Flow<Result<PokemonSpecies>> {
        return flow {
            val detailsResponse = runCatchingFromSuspend {
                remoteDataSource.fetchPokemonDetails(id)
            }

            val details = detailsResponse.map { pokemonSpeciesDetailsDtoMapper.map(it) }
            emit(details)

            detailsResponse.getOrNull()?.let { detailsDto ->
                runCatchingFromSuspend { remoteDataSource.fetchEvolutionChain(detailsDto.evolutionChain.url) }
                    .map { evolutionChainDto ->
                        val evolutionChain = evolutionChainDtoMapper.map(
                            evolutionChainDto,
                            detailsDto.name
                        )
                        val updatedDetails = details.map {
                            it.copy(evolution = evolutionChain)
                        }
                        emit(updatedDetails)
                    }
            }
        }
    }

    override suspend fun getNextPokemonPage(): Result<Unit> {
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
            val feedItems = paginationDto.results.map(pokemonSpeciesFeedItemDtoMapper::map)
            val current = inMemoryPokemonFeed.value
            inMemoryPokemonFeed.emit(current + feedItems)
        }
    }
}
