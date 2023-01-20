package com.assignment.catawiki.pokemon.species.impl

import com.assignment.catawiki.common.runCatchingFromSuspend
import com.assignment.catawiki.pokemon.species.BuildConfig
import com.assignment.catawiki.pokemon.species.api.PokemonSpeciesRepository
import com.assignment.catawiki.pokemon.species.api.model.PokemonDetails
import com.assignment.catawiki.pokemon.species.api.model.PokemonSpeciesFeedItem
import com.assignment.catawiki.pokemon.species.impl.local.PokemonSpeciesFeedPaginationDataSource
import com.assignment.catawiki.pokemon.species.impl.local.model.PaginationData
import com.assignment.catawiki.pokemon.species.impl.mapper.EvolutionChainDtoMapper
import com.assignment.catawiki.pokemon.species.impl.mapper.PokemonSpeciesDetailsDtoMapper
import com.assignment.catawiki.pokemon.species.impl.mapper.PokemonSpeciesFeedItemDtoMapper
import com.assignment.catawiki.pokemon.species.impl.remote.PokemonSpeciesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class PokemonSpeciesRepositoryImpl @Inject constructor(
    private val remoteDataSource: PokemonSpeciesRemoteDataSource,
    private val feedPaginationDataSource: PokemonSpeciesFeedPaginationDataSource,
    private val pokemonSpeciesFeedItemDtoMapper: PokemonSpeciesFeedItemDtoMapper,
    private val pokemonSpeciesDetailsDtoMapper: PokemonSpeciesDetailsDtoMapper,
    private val evolutionChainDtoMapper: EvolutionChainDtoMapper,
) : PokemonSpeciesRepository {

    private val inMemoryPokemonFeed = MutableSharedFlow<List<PokemonSpeciesFeedItem>>()

    override fun getPokemonFeed(): Flow<List<PokemonSpeciesFeedItem>> {
        return inMemoryPokemonFeed
    }

    override fun getPokemonDetails(id: Long): Flow<Result<PokemonDetails>> {
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
            inMemoryPokemonFeed.emit(feedItems)
        }
    }
}
