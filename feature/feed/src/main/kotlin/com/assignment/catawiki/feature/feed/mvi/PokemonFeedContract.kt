package com.assignment.catawiki.feature.feed.mvi

import com.assignment.catawiki.feature.feed.presentation.model.PokemonSpeciesFeedItem
import com.assignment.catawiki.mvi.UiEffect
import com.assignment.catawiki.mvi.UiEvent
import com.assignment.catawiki.mvi.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

interface PokemonFeedContract {

    sealed interface Event : UiEvent {
        object GetFeed : Event
        object GetFeedNextPage : Event
        object GetInitialPage : Event
        object RefreshFeed : Event
        object RetryLoadFeed : Event
    }

    sealed interface Effect : UiEffect {
        object DisplayPaginationLoading : Effect
        object DisplayRefresh : Effect
        object DisplayPaginationFailure : Effect
        object DisplayLoadingFailure : Effect
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
            object LoadingFailed : LoadingError
            object PaginationLoadingFailed : LoadingError
        }
    }


}
