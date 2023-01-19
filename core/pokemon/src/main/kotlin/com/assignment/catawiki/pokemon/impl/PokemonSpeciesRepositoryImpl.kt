package com.assignment.catawiki.pokemon.impl

import com.assignment.catawiki.common.runCatchingFromSuspend
import com.assignment.catawiki.pokemon.BuildConfig
import com.assignment.catawiki.pokemon.api.PokemonSpeciesRepository
import com.assignment.catawiki.pokemon.api.model.PokemonDetails
import com.assignment.catawiki.pokemon.api.model.PokemonSpeciesFeedItem
import com.assignment.catawiki.pokemon.impl.local.PokemonSpeciesFeedPaginationDataSource
import com.assignment.catawiki.pokemon.impl.local.model.PaginationData
import com.assignment.catawiki.pokemon.impl.mapper.PokemonSpeciesFeedItemDtoMapper
import com.assignment.catawiki.pokemon.impl.remote.PokemonSpeciesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class PokemonSpeciesRepositoryImpl @Inject constructor(
    private val remoteDataSource: PokemonSpeciesRemoteDataSource,
    private val feedPaginationDataSource: PokemonSpeciesFeedPaginationDataSource,
    private val pokemonSpeciesFeedItemDtoMapper: PokemonSpeciesFeedItemDtoMapper,
) : PokemonSpeciesRepository {

    private val inMemoryPokemonFeed = MutableSharedFlow<List<PokemonSpeciesFeedItem>>()

    override fun getPokemonFeed(): Flow<Result<List<PokemonSpeciesFeedItem>>> {
        return inMemoryPokemonFeed
            .map { Result.success(it) }
    }

    override fun getPokemonDetails(id: Long): Flow<Result<PokemonDetails>> {
        TODO("Not yet implemented")
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
