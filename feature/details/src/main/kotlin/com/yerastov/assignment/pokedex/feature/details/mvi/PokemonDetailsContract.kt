package com.yerastov.assignment.pokedex.feature.details.mvi

import com.yerastov.assignment.pokedex.core.pokemon.species.domain.model.PokemonSpecies
import com.yerastov.assignment.pokedex.mvi.UiEffect
import com.yerastov.assignment.pokedex.mvi.UiEvent
import com.yerastov.assignment.pokedex.mvi.UiState

interface PokemonDetailsContract {

    sealed interface Event : UiEvent {
        data class GetPokemonSpecies(val id: Long) : Event
        data class GetPokemonDetails(val id: Long) : Event

        data class GetPokemonEvolution(val id: Long) : Event
    }

    sealed interface Effect : UiEffect {
        data class DisplayPokemonDetails(
            val pokemonSpecies: PokemonSpecies,
        ) : Effect
        data class DisplayLoadingDetailsFailed(val id: Long) : Effect
        data class DisplayLoadingEvolutionFailed(val id: Long) : Effect

        object DisplayLoadingDetails : Effect
        object DisplayLoadingEvolution : Effect
    }

    data class State(
        val id: Long = -1,
        val name: String = "",
        val description: String = "",
        val imageUrl: String? = null,
        val captureRateDifference: Int? = null,
        val evolution: PokemonSpecies.Evolution? = null,
        val loadingDetails: Boolean = true,
        val loadingEvolution: Boolean = true,
        val error: Error? = null,
    ) : UiState {

        sealed interface Error {
            data class LoadingDetailsFailed(val id: Long) : Error
            data class LoadingEvolutionFailed(val id: Long) : Error
        }
    }
}
