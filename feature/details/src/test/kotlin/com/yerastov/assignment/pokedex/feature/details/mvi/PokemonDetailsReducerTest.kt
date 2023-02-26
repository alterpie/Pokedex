package com.yerastov.assignment.pokedex.feature.details.mvi

import com.yerastov.assignment.pokedex.core.pokemon.species.domain.model.PokemonSpecies
import com.yerastov.assignment.pokedex.feature.details.mvi.PokemonDetailsContract.Effect
import com.yerastov.assignment.pokedex.feature.details.mvi.PokemonDetailsContract.State
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PokemonDetailsReducerTest {

    @Test
    fun `should display loading details failed`() {
        val initialState = State(
            loadingDetails = true,
            loadingEvolution = true,
            error = null
        )
        val reducer = PokemonDetailsReducer()

        val newState = reducer.invoke(initialState, Effect.DisplayLoadingDetailsFailed(42L))

        newState shouldBe initialState.copy(
            loadingDetails = false,
            loadingEvolution = false,
            error = State.Error.LoadingDetailsFailed(42L)
        )
    }

    @Test
    fun `should display loading evolution failed`() {
        val initialState = State(loadingEvolution = true, error = null)
        val reducer = PokemonDetailsReducer()

        val newState = reducer.invoke(initialState, Effect.DisplayLoadingEvolutionFailed(42L))

        newState shouldBe initialState.copy(
            loadingEvolution = false,
            error = State.Error.LoadingEvolutionFailed(42L)
        )
    }

    @Test
    fun `should display loading details`() {
        val initialState = State(
            loadingDetails = false,
            loadingEvolution = false,
            error = State.Error.LoadingDetailsFailed(42L)
        )
        val reducer = PokemonDetailsReducer()

        val newState = reducer.invoke(initialState, Effect.DisplayLoadingDetails)

        newState shouldBe initialState.copy(
            loadingEvolution = true,
            loadingDetails = true,
            error = null,
        )
    }

    @Test
    fun `should display loading evolution`() {
        val initialState = State(
            loadingEvolution = false,
            error = State.Error.LoadingEvolutionFailed(42L)
        )
        val reducer = PokemonDetailsReducer()

        val newState = reducer.invoke(initialState, Effect.DisplayLoadingDetails)

        newState shouldBe initialState.copy(
            loadingEvolution = true,
            error = null,
        )
    }

    @Nested
    inner class TestDisplaySpecies {

        @Test
        fun `should display details data`() {
            val initialState = State()
            val reducer = PokemonDetailsReducer()

            val newState = reducer.invoke(
                initialState,
                Effect.DisplayPokemonDetails(buildPokemonSpecies().copy(evolution = PokemonSpecies.Evolution.Final))
            )

            newState.id shouldBe 42
            newState.name shouldBe "name"
            newState.description shouldBe "description"
            newState.captureRateDifference shouldBe 42
            newState.imageUrl shouldBe "image"
            newState.evolution shouldBe PokemonSpecies.Evolution.Final
        }

        @Test
        fun `should display loading details when description is absent`() {
            val initialState = State(loadingDetails = false)
            val reducer = PokemonDetailsReducer()

            val newState = reducer.invoke(
                initialState,
                Effect.DisplayPokemonDetails(buildPokemonSpecies().copy(description = null))
            )

            newState.loadingDetails shouldBe true
        }

        @Test
        fun `should not display loading details when description is present`() {
            val initialState = State(loadingDetails = false)
            val reducer = PokemonDetailsReducer()

            val newState = reducer.invoke(
                initialState,
                Effect.DisplayPokemonDetails(buildPokemonSpecies().copy(description = "desc"))
            )

            newState.loadingDetails shouldBe false
        }

        @Test
        fun `should display loading evolution when evolution is absent`() {
            val initialState = State(loadingEvolution = false)
            val reducer = PokemonDetailsReducer()

            val newState = reducer.invoke(
                initialState,
                Effect.DisplayPokemonDetails(buildPokemonSpecies().copy(evolution = null))
            )

            newState.loadingEvolution shouldBe true
        }

        @Test
        fun `should not display loading evolution when evolution is present`() {
            val initialState = State(loadingEvolution = false)
            val reducer = PokemonDetailsReducer()

            val newState = reducer.invoke(
                initialState,
                Effect.DisplayPokemonDetails(buildPokemonSpecies().copy(evolution = PokemonSpecies.Evolution.Final))
            )

            newState.loadingEvolution shouldBe false
        }
    }

    private fun buildPokemonSpecies(): PokemonSpecies {
        return PokemonSpecies(42L, "name", "image", "description", 42, null)
    }
}
