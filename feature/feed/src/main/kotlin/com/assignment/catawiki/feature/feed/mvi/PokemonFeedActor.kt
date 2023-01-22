package com.assignment.catawiki.feature.feed.mvi

import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.Effect
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.Event
import com.assignment.catawiki.feature.feed.presentation.usecase.GetPokemonFeedUseCase
import com.assignment.catawiki.mvi.Actor
import com.assignment.catawiki.pokemon.species.domain.PokemonSpeciesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PokemonFeedActor @Inject constructor(
    private val getPokemonFeedUseCase: GetPokemonFeedUseCase,
    private val pokemonSpeciesRepository: PokemonSpeciesRepository,
) : Actor<Event, Effect> {

    override fun invoke(event: Event): Flow<Effect> = when (event) {
        Event.GetFeed -> getPokemonFeedUseCase.execute()
            .map(Effect::DisplayPokemonFeed)
        Event.GetFeedNextPage -> flow {
            emit(Effect.DisplayPaginationLoading)
            pokemonSpeciesRepository.getNextSpeciesPage(false)
                .onFailure { emit(Effect.DisplayPaginationFailure) }
        }
        Event.RefreshFeed -> refreshPage()
        Event.GetInitialPage -> flow {
            if (pokemonSpeciesRepository.getStoredItemsCount() == 0L) {
                emit(Effect.DisplayRefresh)
                pokemonSpeciesRepository.getNextSpeciesPage(false)
                    .onFailure { emit(Effect.DisplayLoadingFailure) }
            }
        }
        Event.RetryLoadFeed -> refreshPage()
    }

    private fun refreshPage(): Flow<Effect> = flow {
        emit(Effect.DisplayRefresh)
        pokemonSpeciesRepository.getNextSpeciesPage(true)
            .onFailure { emit(Effect.DisplayLoadingFailure) }
    }
}
