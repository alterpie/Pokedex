package com.assignment.catawiki.details.mvi

import com.assignment.catawiki.details.mvi.PokemonDetailsContract.Effect
import com.assignment.catawiki.details.mvi.PokemonDetailsContract.Event
import com.assignment.catawiki.details.mvi.PokemonDetailsContract.State
import com.assignment.catawiki.mvi.Feature
import javax.inject.Inject

class PokemonDetailsFeature @Inject constructor(
    actor: PokemonDetailsActor,
    reducer: PokemonDetailsReducer,
) : Feature<Event, Effect, State>(State(), actor, reducer) {
}
