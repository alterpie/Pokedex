package com.assignment.catawiki.feature.feed.presentation

import androidx.lifecycle.SavedStateHandle
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.Effect
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.Event
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.State
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedFeature
import com.assignment.catawiki.mvi.viewModel.BaseViewModel

class PokemonFeedViewModel(
    savedStateHandle: SavedStateHandle,
    pokemonFeedFeature: PokemonFeedFeature,
) : BaseViewModel<Event, Effect, State>(pokemonFeedFeature) {

    init {
        onEvent(Event.GetPokemonFeed)
        onEvent(Event.GetPokemonFeedNextPage)
    }
}
