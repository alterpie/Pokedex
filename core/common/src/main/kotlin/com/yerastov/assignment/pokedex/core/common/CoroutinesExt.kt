package com.yerastov.assignment.pokedex.core.common

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException

suspend fun <T> runCatchingFromSuspend(body: suspend () -> T): Result<T> {
    return runCatching { body() }
        .onFailure { if (it is CancellationException && it !is TimeoutCancellationException) throw it }
}
