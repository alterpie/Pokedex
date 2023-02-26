package com.yerastov.assignment.pokedex.feature.feed.mvi

import com.yerastov.assignment.pokedex.feature.feed.presentation.model.PokemonSpeciesFeedItem
import com.yerastov.assignment.pokedex.mvi.UiEffect
import com.yerastov.assignment.pokedex.mvi.UiEvent
import com.yerastov.assignment.pokedex.mvi.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

interface PokemonFeedContract {

    sealed interface Event : UiEvent {
        object GetFeed : Event
        object GetFeedNextPage : Event
        object GetInitialPage : Event
        object RefreshFeed : Event
        object RetryLoadFeed : Event
        object AcknowledgePopupErrorShown : Event
    }

    sealed interface Effect : UiEffect {
        object DisplayPaginationLoading : Effect
        object DisplayRefresh : Effect
        object DisplayPaginationFailure : Effect
        object DisplayLoadingFailure : Effect
        object AcknowledgePopupError : Effect

        data class DisplayPokemonFeed(val feed: List<PokemonSpeciesFeedItem>) : Effect
    }

    data class State(
        val items: ImmutableList<PokemonSpeciesFeedItem> = persistentListOf(),
        val loadingError: LoadingError? = null,
        val loadingState: LoadingState? = null,
    ) : UiState {

        sealed interface LoadingState {
            object Pagination : LoadingState
            object Refresh : LoadingState
        }

        sealed interface LoadingError {
            object InitialFailed : LoadingError
            object RefreshFailed : LoadingError
            object PaginationFailed : LoadingError
        }
    }


}
