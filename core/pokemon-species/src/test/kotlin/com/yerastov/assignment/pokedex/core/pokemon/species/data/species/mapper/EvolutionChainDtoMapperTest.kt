package com.yerastov.assignment.pokedex.core.pokemon.species.data.species.mapper

import android.net.Uri
import com.yerastov.assignment.pokedex.core.pokemon.species.BuildConfig
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.model.SpeciesEntity
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.remote.model.EvolutionChainDto
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class EvolutionChainDtoMapperTest {

    @BeforeEach
    fun beforeEach() {
        mockkStatic(Uri::class)
    }

    @AfterEach
    fun afterEach() {
        unmockkStatic(Uri::class)
    }

    @Test
    fun `should map next evolution of initial form of pokemon when has next evolution form`() {
        every { Uri.parse(any()) } returns mockk {
            every { pathSegments } returns listOf("api", "1")
        }
        val dto = buildDto()
        val mapper = EvolutionChainDtoMapper()

        val mapped = mapper.map(dto, "charmander")

        mapped shouldBe SpeciesEntity.Evolution.EvolvesTo(
            1L,
            "charmeleon",
            "${BuildConfig.POKEMON_IMAGE_URL}1.png"
        )
    }

    @Test
    fun `should map final evolution of initial form of pokemon when does not have next evolution form`() {
        val dto = EvolutionChainDto(
            EvolutionChainDto.Chain(
                emptyList(),
                EvolutionChainDto.Chain.Species("final form pokemon", "https://host.com/api/3")
            )
        )
        val mapper = EvolutionChainDtoMapper()

        val mapped = mapper.map(dto, "final form pokemon")

        mapped shouldBe SpeciesEntity.Evolution.Final
    }

    @Test
    fun `should map next evolution for nested species when it has next evolution form`() {
        every { Uri.parse(any()) } returns mockk {
            every { pathSegments } returns listOf("api", "2")
        }
        val dto = buildDto()
        val mapper = EvolutionChainDtoMapper()

        val mapped = mapper.map(dto, "charmeleon")

        mapped shouldBe SpeciesEntity.Evolution.EvolvesTo(
            2L,
            "charizard",
            "${BuildConfig.POKEMON_IMAGE_URL}2.png"
        )
    }

    @Test
    fun `should map final evolution for nested species when it does not have next evolution form`() {
        val dto = buildDto()
        val mapper = EvolutionChainDtoMapper()

        val mapped = mapper.map(dto, "charizard")

        mapped shouldBe SpeciesEntity.Evolution.Final
    }

    private fun buildDto() = EvolutionChainDto(
        EvolutionChainDto.Chain(
            listOf(
                EvolutionChainDto.Chain.EvolutionTarget(
                    listOf(
                        EvolutionChainDto.Chain.EvolutionTarget(
                            emptyList(),
                            EvolutionChainDto.Chain.Species("charizard", "https://host.com/api/3")
                        )
                    ),
                    EvolutionChainDto.Chain.Species("charmeleon", "https://host.com/api/2")
                )
            ),
            EvolutionChainDto.Chain.Species(
                "charmander",
                "https://host.com/api/1",
            )
        )
    )
}
