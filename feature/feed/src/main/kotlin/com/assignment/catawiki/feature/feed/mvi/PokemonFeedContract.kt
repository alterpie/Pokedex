package com.assignment.catawiki.feature.feed.mvi

import com.assignment.catawiki.mvi.UiEffect
import com.assignment.catawiki.mvi.UiEvent
import com.assignment.catawiki.mvi.UiState
import com.assignment.catawiki.pokemon.species.api.model.PokemonSpeciesFeedItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

interface PokemonFeedContract {

    sealed interface Event : UiEvent {
        object GetPokemonFeed : Event
        object GetPokemonFeedNextPage : Event
    }

    sealed interface Effect : UiEffect {
        object DisplayLoadingFailure : Effect
        data class DisplayPokemonFeed(val feed: List<PokemonSpeciesFeedItem>) : Effect
    }

    data class State(
        val items: ImmutableList<PokemonSpeciesFeedItem> = persistentListOf(),
        val loadingError: LoadingError? = null,
    ) : UiState {

        sealed interface LoadingError {
            object PaginationLoadingFailed : LoadingError
        }
    }


}
