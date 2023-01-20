package com.assignment.catawiki.mvi

fun interface Reducer<Effect : UiEffect, State : UiState> {
    fun invoke(currentState: State, effect: Effect): State
}
