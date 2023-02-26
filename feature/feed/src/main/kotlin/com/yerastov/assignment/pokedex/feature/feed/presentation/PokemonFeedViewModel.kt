package com.yerastov.assignment.pokedex.feature.feed.presentation

import androidx.lifecycle.SavedStateHandle
import com.yerastov.assignment.pokedex.feature.feed.mvi.PokemonFeedContract.Effect
import com.yerastov.assignment.pokedex.feature.feed.mvi.PokemonFeedContract.Event
import com.yerastov.assignment.pokedex.feature.feed.mvi.PokemonFeedContract.State
import com.yerastov.assignment.pokedex.feature.feed.mvi.PokemonFeedFeature
import com.yerastov.assignment.pokedex.mvi.viewModel.BaseViewModel

class PokemonFeedViewModel(
    savedStateHandle: SavedStateHandle,
    pokemonFeedFeature: PokemonFeedFeature,
) : BaseViewModel<Event, Effect, State>(pokemonFeedFeature) {

    init {
        onEvent(Event.GetFeed)
        onEvent(Event.GetInitialPage)
    }
}
