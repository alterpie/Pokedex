package com.assignment.catawiki.feature.feed.mvi

import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.Effect
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.State
import com.assignment.catawiki.mvi.Reducer
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList

class PokemonFeedReducer @Inject constructor() : Reducer<Effect, State> {

    override fun invoke(currentState: State, effect: Effect): State = when (effect) {
        is Effect.DisplayPokemonFeed -> currentState.copy(
            items = effect.feed.toImmutableList(),
            loadingState = null
        )
        Effect.DisplayPaginationFailure -> currentState.copy(
            loadingError = State.LoadingError.PaginationFailed,
            loadingState = null,
        )
        Effect.DisplayLoadingFailure -> currentState.copy(
            loadingError = if (currentState.items.isEmpty()) {
                State.LoadingError.InitialFailed
            } else {
                State.LoadingError.RefreshFailed
            },
            loadingState = null,
        )
        Effect.DisplayPaginationLoading -> currentState.copy(
            loadingState = State.LoadingState.Pagination,
            loadingError = null,
        )
        Effect.DisplayRefresh -> currentState.copy(
            loadingState = State.LoadingState.Refresh,
            loadingError = null,
        )
        Effect.AcknowledgePopupError -> currentState.copy(
            loadingError = null,
        )
    }
}
