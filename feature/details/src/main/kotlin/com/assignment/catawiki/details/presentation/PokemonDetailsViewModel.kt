package com.assignment.catawiki.details.presentation

import androidx.lifecycle.SavedStateHandle
import com.assignment.catawiki.details.mvi.PokemonDetailsContract.Effect
import com.assignment.catawiki.details.mvi.PokemonDetailsContract.Event
import com.assignment.catawiki.details.mvi.PokemonDetailsContract.State
import com.assignment.catawiki.details.mvi.PokemonDetailsFeature
import com.assignment.catawiki.details.navigation.POKEMON_ID_NAV_PARAM
import com.assignment.catawiki.mvi.viewModel.BaseViewModel

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
