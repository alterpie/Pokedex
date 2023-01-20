package com.assignment.catawiki.details.mvi

import com.assignment.catawiki.mvi.UiEffect
import com.assignment.catawiki.mvi.UiEvent
import com.assignment.catawiki.mvi.UiState
import com.assignment.catawiki.pokemon.species.api.model.PokemonDetails

interface PokemonDetailsContract {

    sealed interface Event : UiEvent {
        data class GetPokemonDetails(val id: Long) : Event
    }

    sealed interface Effect : UiEffect {
        data class DisplayPokemonDetails(
            val pokemonDetails: PokemonDetails,
        ) : Effect

        object DisplayError : Effect // temporary
    }

    data class State(
        val name: String = "",
        val description: String = "",
        val captureRate: Int? = null,
        val evolvesIntoName: String? = null,
        val evolvesIntoImage: String? = null,
    ) : UiState
}
