package com.assignment.catawiki.mvi

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.DEFAULT_CONCURRENCY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.update

abstract class Feature<Event : UiEvent, Effect : UiEffect, State : UiState>(
    val initialState: State,
    private val actor: Actor<Event, Effect>,
    private val reducer: Reducer<Effect, State>,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val stateFlow = MutableStateFlow(initialState)
    private val eventFlow = MutableSharedFlow<Event>()

    fun collectState(coroutineScope: CoroutineScope): Flow<State> {
        return eventFlow
            .produceIn(coroutineScope)
            .consumeAsFlow()
            .flatMapMerge(DEFAULT_CONCURRENCY, actor::invoke)
            .flowOn(coroutineDispatcher)
            .scan(initialState) { state, effect -> reducer.invoke(state, effect) }
            .onEach { state -> stateFlow.update { state } }
    }

    suspend fun onEvent(event: Event) {
        eventFlow.emit(event)
    }
}
