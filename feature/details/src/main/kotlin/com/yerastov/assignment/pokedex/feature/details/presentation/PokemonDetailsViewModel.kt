package com.yerastov.assignment.pokedex.feature.details.presentation

import androidx.lifecycle.SavedStateHandle
import com.yerastov.assignment.pokedex.feature.details.mvi.PokemonDetailsContract.Effect
import com.yerastov.assignment.pokedex.feature.details.mvi.PokemonDetailsContract.Event
import com.yerastov.assignment.pokedex.feature.details.mvi.PokemonDetailsContract.State
import com.yerastov.assignment.pokedex.feature.details.mvi.PokemonDetailsFeature
import com.yerastov.assignment.pokedex.feature.details.navigation.POKEMON_ID_NAV_PARAM
import com.yerastov.assignment.pokedex.mvi.viewModel.BaseViewModel

class PokemonDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    feature: PokemonDetailsFeature,
) : BaseViewModel<Event, Effect, State>(feature) {

    init {
        val speciesId = requireNotNull(savedStateHandle.get<Long>(POKEMON_ID_NAV_PARAM))
        onEvent(Event.GetPokemonSpecies(speciesId))
        onEvent(Event.GetPokemonDetails(speciesId))
    }
}
