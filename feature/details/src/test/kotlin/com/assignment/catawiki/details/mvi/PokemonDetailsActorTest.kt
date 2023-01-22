package com.assignment.catawiki.details.mvi

import app.cash.turbine.test
import com.assignment.catawiki.details.mvi.PokemonDetailsContract.Effect
import com.assignment.catawiki.details.mvi.PokemonDetailsContract.Event
import com.assignment.catawiki.pokemon.species.domain.PokemonSpeciesRepository
import com.assignment.catawiki.pokemon.species.domain.error.GetSpeciesDetailsError
import com.assignment.catawiki.pokemon.species.domain.error.GetSpeciesEvolutionError
import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpecies
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PokemonDetailsActorTest {

    @Test
    fun `should display pokemon species updates`() = runTest {
        val speciesEmissions =
            flowOf(buildPokemonSpecies(), buildPokemonSpecies().copy(name = "other name"))
        val repository = mockk<PokemonSpeciesRepository> {
            every { getPokemonSpecies(any()) } returns speciesEmissions
        }
        val actor = buildActor(repository)

        actor.invoke(Event.GetPokemonSpecies(42L))
            .test {
                awaitItem() shouldBe Effect.DisplayPokemonDetails(buildPokemonSpecies())
                awaitItem() shouldBe Effect.DisplayPokemonDetails(buildPokemonSpecies().copy(name = "other name"))
                awaitComplete()
            }
    }

    @Nested
    inner class TestGetPokemonDetails {
        @Test
        fun `should display loading when getting species details`() = runTest {
            val repository = mockk<PokemonSpeciesRepository> {
                coEvery { getSpeciesDetails(any()) } returns Result.success(Unit)
            }
            val actor = buildActor(repository)

            actor.invoke(Event.GetPokemonDetails(42L))
                .test {
                    awaitItem() shouldBe Effect.DisplayLoadingDetails
                    awaitComplete()
                }
        }

        @Test
        fun `should display error for failed details loading attempt`() = runTest {
            val repository = mockk<PokemonSpeciesRepository> {
                coEvery { getSpeciesDetails(any()) } returns Result.failure(GetSpeciesDetailsError())
            }
            val actor = buildActor(repository)

            actor.invoke(Event.GetPokemonDetails(42L))
                .test {
                    awaitItem() shouldBe Effect.DisplayLoadingDetails
                    awaitItem() shouldBe Effect.DisplayLoadingDetailsFailed(42L)
                    awaitComplete()
                }
        }

        @Test
        fun `should display error for failed evolution loading attempt`() = runTest {
            val repository = mockk<PokemonSpeciesRepository> {
                coEvery { getSpeciesDetails(any()) } returns Result.failure(GetSpeciesEvolutionError())
            }
            val actor = buildActor(repository)

            actor.invoke(Event.GetPokemonDetails(42L))
                .test {
                    awaitItem() shouldBe Effect.DisplayLoadingDetails
                    awaitItem() shouldBe Effect.DisplayLoadingEvolutionFailed(42L)
                    awaitComplete()
                }
        }
    }

    @Nested
    inner class TestGetPokemonEvolution {
        @Test
        fun `should display loading when getting species evolution`() = runTest {
            val repository = mockk<PokemonSpeciesRepository> {
                coEvery { getSpeciesEvolution(any()) } returns Result.success(Unit)
            }
            val actor = buildActor(repository)

            actor.invoke(Event.GetPokemonEvolution(42L))
                .test {
                    awaitItem() shouldBe Effect.DisplayLoadingEvolution
                    awaitComplete()
                }
        }

        @Test
        fun `should display error for failed evolution loading attempt`() = runTest {
            val repository = mockk<PokemonSpeciesRepository> {
                coEvery {
                    getSpeciesEvolution(any())
                } returns Result.failure(GetSpeciesEvolutionError())
            }
            val actor = buildActor(repository)

            actor.invoke(Event.GetPokemonEvolution(42L))
                .test {
                    awaitItem() shouldBe Effect.DisplayLoadingEvolution
                    awaitItem() shouldBe Effect.DisplayLoadingEvolutionFailed(42L)
                    awaitComplete()
                }
        }
    }

    private fun buildActor(pokemonSpeciesRepository: PokemonSpeciesRepository): PokemonDetailsActor {
        return PokemonDetailsActor(pokemonSpeciesRepository)
    }

    private fun buildPokemonSpecies(): PokemonSpecies {
        return PokemonSpecies(42L, "name", "image", "description", 42, null)
    }
}
