package com.yerastov.assignment.pokedex.feature.feed.mvi

import com.yerastov.assignment.pokedex.feature.feed.mvi.PokemonFeedContract.Effect
import com.yerastov.assignment.pokedex.feature.feed.mvi.PokemonFeedContract.State
import com.yerastov.assignment.pokedex.feature.feed.presentation.model.PokemonSpeciesFeedItem
import io.kotest.matchers.shouldBe
import io.mockk.mockk
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
            State(loadingState = null, loadingError = State.LoadingError.PaginationFailed)
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
            loadingError = State.LoadingError.PaginationFailed,
        )
    }

    @Test
    fun `should display refresh loading`() {
        val initialState =
            State(loadingState = null, loadingError = State.LoadingError.InitialFailed)
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
    fun `should display loading failure as initial when items list is empty`() {
        val initialState = State(
            loadingState = State.LoadingState.Refresh,
            loadingError = null,
            items = persistentListOf()
        )
        val reducer = PokemonFeedReducer()

        val newState = reducer.invoke(
            initialState,
            Effect.DisplayLoadingFailure
        )

        newState shouldBe initialState.copy(
            loadingState = null,
            loadingError = State.LoadingError.InitialFailed
        )
    }

    @Test
    fun `should display loading failure as refresh when items list is not empty`() {
        val initialState = State(
            loadingState = State.LoadingState.Refresh,
            loadingError = null,
            items = persistentListOf(mockk(), mockk())
        )
        val reducer = PokemonFeedReducer()

        val newState = reducer.invoke(
            initialState,
            Effect.DisplayLoadingFailure
        )

        newState shouldBe initialState.copy(
            loadingState = null,
            loadingError = State.LoadingError.RefreshFailed
        )
    }

    @Test
    fun `should acknowledge popup error`() {
        val initialState = State(loadingError = State.LoadingError.RefreshFailed)
        val reducer = PokemonFeedReducer()

        val newState = reducer.invoke(
            initialState,
            Effect.AcknowledgePopupError
        )

        newState shouldBe initialState.copy(
            loadingError = null
        )
    }

    private fun buildFeedItem(): PokemonSpeciesFeedItem {
        return PokemonSpeciesFeedItem(42L, "name", "image")
    }
}
