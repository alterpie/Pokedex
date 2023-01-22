package com.assignment.catawiki.feature.feed.presentation.usecase

import app.cash.turbine.test
import com.assignment.catawiki.feature.feed.presentation.mapper.PokemonSpeciesFeedItemMapper
import com.assignment.catawiki.pokemon.species.domain.PokemonSpeciesRepository
import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpecies
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class GetPokemonFeedUseCaseTest {

    @Test
    fun `should map domain data to feed items`() = runTest {
        val pokemonSpeciesRepository = mockk<PokemonSpeciesRepository> {
            every { getAllPokemonSpecies() } returns flowOf(
                listOf(
                    buildPokemonSpecies(),
                    buildPokemonSpecies().copy(id = 43L)
                )
            )
        }
        val mapper = mockk<PokemonSpeciesFeedItemMapper> {
            coEvery { map(any()) } returns mockk()
        }
        val useCase = GetPokemonFeedUseCase(pokemonSpeciesRepository, mapper)

        useCase.execute()
            .test {
                awaitItem().size shouldBe 2
                awaitComplete()
            }

        coVerifyOrder {
            pokemonSpeciesRepository.getAllPokemonSpecies()
            mapper.map(buildPokemonSpecies())
            mapper.map(buildPokemonSpecies().copy(id = 43L))
        }
    }

    private fun buildPokemonSpecies(): PokemonSpecies {
        return PokemonSpecies(42L, "name", "image", "description", 42, null)
    }
}
