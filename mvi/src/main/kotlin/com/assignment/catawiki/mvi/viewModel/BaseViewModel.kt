package com.assignment.catawiki.mvi.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.catawiki.mvi.Feature
import com.assignment.catawiki.mvi.UiEffect
import com.assignment.catawiki.mvi.UiEvent
import com.assignment.catawiki.mvi.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<Event : UiEvent, Effect : UiEffect, State : UiState>(
    private val feature: Feature<Event, Effect, State>
) : ViewModel() {

    private val _state = MutableStateFlow(feature.initialState)
    val state: StateFlow<State> = _state

    init {
        feature.collectState(viewModelScope)
            .onEach { state -> _state.update { state } }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: Event) {
        viewModelScope.launch { feature.onEvent(event) }
    }
}
