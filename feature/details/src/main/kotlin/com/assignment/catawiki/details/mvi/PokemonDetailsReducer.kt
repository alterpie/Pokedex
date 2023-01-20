package com.assignment.catawiki.details.mvi

import com.assignment.catawiki.details.mvi.PokemonDetailsContract.Effect
import com.assignment.catawiki.details.mvi.PokemonDetailsContract.State
import com.assignment.catawiki.mvi.Reducer
import javax.inject.Inject

class PokemonDetailsReducer @Inject constructor() : Reducer<Effect, State> {

    override fun invoke(currentState: State, effect: Effect): State = when (effect) {
        Effect.DisplayError -> currentState
        is Effect.DisplayPokemonDetails -> currentState.copy(
            name = effect.pokemonSpecies.name,
            description = effect.pokemonSpecies.description ?: "",
            captureRate = effect.pokemonSpecies.captureRate,
            evolution = effect.pokemonSpecies.evolution,
        )
    }
}
