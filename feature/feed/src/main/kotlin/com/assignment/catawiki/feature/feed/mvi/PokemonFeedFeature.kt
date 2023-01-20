package com.assignment.catawiki.feature.feed.mvi

import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.Effect
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.Event
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.State
import com.assignment.catawiki.mvi.Feature
import javax.inject.Inject

class PokemonFeedFeature @Inject constructor(
    actor: PokemonFeedActor,
    reducer: PokemonFeedReducer,
) : Feature<Event, Effect, State>(State(), actor, reducer)
