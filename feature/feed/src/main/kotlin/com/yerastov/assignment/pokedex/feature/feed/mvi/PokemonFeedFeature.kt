package com.yerastov.assignment.pokedex.feature.feed.mvi

import com.yerastov.assignment.pokedex.feature.feed.mvi.PokemonFeedContract.Effect
import com.yerastov.assignment.pokedex.feature.feed.mvi.PokemonFeedContract.Event
import com.yerastov.assignment.pokedex.feature.feed.mvi.PokemonFeedContract.State
import com.yerastov.assignment.pokedex.mvi.Feature
import javax.inject.Inject

class PokemonFeedFeature @Inject constructor(
    actor: PokemonFeedActor,
    reducer: PokemonFeedReducer,
) : Feature<Event, Effect, State>(State(), actor, reducer)
