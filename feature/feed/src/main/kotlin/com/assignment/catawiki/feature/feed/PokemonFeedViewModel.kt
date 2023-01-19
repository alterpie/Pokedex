package com.assignment.catawiki.feature.feed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.catawiki.feature.feed.PokemonFeedContract.State
import com.assignment.catawiki.pokemon.species.api.PokemonSpeciesRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

internal class PokemonFeedViewModel(
    savedStateHandle: SavedStateHandle,
    private val pokemonSpeciesRepository: PokemonSpeciesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    init {
        pokemonSpeciesRepository.getPokemonFeed()
            .onEach { feed ->
                _state.update { it.copy(items = feed.toImmutableList()) }
            }
            .launchIn(viewModelScope)
    }
}
