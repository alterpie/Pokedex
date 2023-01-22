package com.assignment.catawiki.feature.feed.mvi

import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.Effect
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.State
import com.assignment.catawiki.feature.feed.presentation.model.PokemonSpeciesFeedItem
import io.kotest.matchers.shouldBe
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.junit.jupiter.api.Test

internal class PokemonFeedReducerTest {

    @Test
    fun `should display feed`() {
        val initialState =
            State(items = persistentListOf(), loadingState = State.LoadingState.Refresh)
        val reducer = PokemonFeedReducer()

        val newState = reducer.invoke(
            initialState,
            Effect.DisplayPokemonFeed(listOf(buildFeedItem()))
        )

        newState shouldBe initialState.copy(
            items = listOf(buildFeedItem()).toImmutableList(),
            loadingState = null
        )
    }

    @Test
    fun `should display pagination loading`() {
        val initialState =
            State(loadingState = null, loadingError = State.LoadingError.PaginationLoadingFailed)
        val reducer = PokemonFeedReducer()

        val newState = reducer.invoke(
            initialState,
            Effect.DisplayPaginationLoading
        )

        newState shouldBe initialState.copy(
            loadingState = State.LoadingState.Pagination,
            loadingError = null
        )
    }

    @Test
    fun `should display pagination loading failed`() {
        val initialState =
            State(loadingState = State.LoadingState.Pagination, loadingError = null)
        val reducer = PokemonFeedReducer()

        val newState = reducer.invoke(
            initialState,
            Effect.DisplayPaginationFailure
        )

        newState shouldBe initialState.copy(
            loadingState = null,
            loadingError = State.LoadingError.PaginationLoadingFailed,
        )
    }

    @Test
    fun `should display refresh loading`() {
        val initialState =
            State(loadingState = null, loadingError = State.LoadingError.LoadingFailed)
        val reducer = PokemonFeedReducer()

        val newState = reducer.invoke(
            initialState,
            Effect.DisplayRefresh
        )

        newState shouldBe initialState.copy(
            loadingState = State.LoadingState.Refresh,
            loadingError = null
        )
    }

    @Test
    fun `should display refresh loading failure`() {
        val initialState =
            State(loadingState = State.LoadingState.Refresh, loadingError = null)
        val reducer = PokemonFeedReducer()

        val newState = reducer.invoke(
            initialState,
            Effect.DisplayLoadingFailure
        )

        newState shouldBe initialState.copy(
            loadingState = null,
            loadingError = State.LoadingError.LoadingFailed
        )
    }

    private fun buildFeedItem(): PokemonSpeciesFeedItem {
        return PokemonSpeciesFeedItem(42L, "name", "image")
    }
}
