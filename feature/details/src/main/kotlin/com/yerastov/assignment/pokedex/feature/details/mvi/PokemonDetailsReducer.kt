package com.yerastov.assignment.pokedex.feature.details.mvi

import com.yerastov.assignment.pokedex.feature.details.mvi.PokemonDetailsContract.Effect
import com.yerastov.assignment.pokedex.feature.details.mvi.PokemonDetailsContract.State
import com.yerastov.assignment.pokedex.mvi.Reducer
import javax.inject.Inject

class PokemonDetailsReducer @Inject constructor() : Reducer<Effect, State> {

    override fun invoke(currentState: State, effect: Effect): State = when (effect) {
        is Effect.DisplayPokemonDetails -> {
            val loadingDetails =
                effect.pokemonSpecies.description == null && currentState.error == null
            val loadingEvolution =
                effect.pokemonSpecies.evolution == null && currentState.error == null
            currentState.copy(
                id = effect.pokemonSpecies.id,
                name = effect.pokemonSpecies.name,
                description = effect.pokemonSpecies.description ?: "",
                captureRateDifference = effect.pokemonSpecies.captureRateDifference,
                evolution = effect.pokemonSpecies.evolution,
                imageUrl = effect.pokemonSpecies.imageUrl,
                loadingDetails = loadingDetails,
                loadingEvolution = loadingEvolution,
            )
        }
        is Effect.DisplayLoadingDetailsFailed -> currentState.copy(
            loadingDetails = false,
            loadingEvolution = false,
            error = State.Error.LoadingDetailsFailed(effect.id),
        )
        is Effect.DisplayLoadingEvolutionFailed -> currentState.copy(
            loadingEvolution = false,
            error = State.Error.LoadingEvolutionFailed(effect.id),
        )
        Effect.DisplayLoadingDetails -> currentState.copy(
            loadingDetails = true,
            loadingEvolution = true,
            error = null,
        )
        Effect.DisplayLoadingEvolution -> currentState.copy(
            loadingEvolution = true,
            error = null,
        )
    }
}
