package com.assignment.catawiki.feature.feed.mvi

import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.Effect
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.State
import com.assignment.catawiki.mvi.Reducer
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class PokemonFeedReducer @Inject constructor() : Reducer<Effect, State> {

    override fun invoke(currentState: State, effect: Effect): State = when (effect) {
        is Effect.DisplayPokemonFeed -> currentState.copy(items = effect.feed.toImmutableList())
        Effect.DisplayLoadingFailure -> TODO()
    }
}
