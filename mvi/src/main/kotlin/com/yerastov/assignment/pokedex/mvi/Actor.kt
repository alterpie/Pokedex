package com.yerastov.assignment.pokedex.mvi

import kotlinx.coroutines.flow.Flow

fun interface Actor<Event : UiEvent, Effect : UiEffect> {
    fun invoke(event: Event): Flow<Effect>
}
