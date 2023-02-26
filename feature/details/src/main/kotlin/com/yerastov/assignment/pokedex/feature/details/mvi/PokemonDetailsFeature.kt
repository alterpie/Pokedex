package com.yerastov.assignment.pokedex.feature.details.mvi

import com.yerastov.assignment.pokedex.feature.details.mvi.PokemonDetailsContract.Effect
import com.yerastov.assignment.pokedex.feature.details.mvi.PokemonDetailsContract.Event
import com.yerastov.assignment.pokedex.feature.details.mvi.PokemonDetailsContract.State
import com.yerastov.assignment.pokedex.mvi.Feature
import javax.inject.Inject

class PokemonDetailsFeature @Inject constructor(
    actor: PokemonDetailsActor,
    reducer: PokemonDetailsReducer,
) : Feature<Event, Effect, State>(State(), actor, reducer) {
}
