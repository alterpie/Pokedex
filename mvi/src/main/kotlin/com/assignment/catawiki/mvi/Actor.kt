package com.assignment.catawiki.mvi

import kotlinx.coroutines.flow.Flow

fun interface Actor<Event : UiEvent, Effect : UiEffect> {
    fun invoke(event: Event): Flow<Effect>
}
