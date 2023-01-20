package com.assignment.catawiki.pokemon.species.impl.mapper

import com.assignment.catawiki.pokemon.species.data.species.mapper.PokemonSpeciesDetailsDtoMapper
import com.assignment.catawiki.pokemon.species.domain.model.PokemonDetails
import com.assignment.catawiki.pokemon.species.data.species.remote.model.PokemonSpeciesDetailsDto
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test


internal class PokemonSpeciesDetailsDtoMapperTest {

    @Test
    fun `should map details with english language description`() {
        val dto = PokemonSpeciesDetailsDto(
            42L,
            "name",
            listOf(
                PokemonSpeciesDetailsDto.FlavorTextEntry(
                    "goede morgen",
                    PokemonSpeciesDetailsDto.FlavorTextEntry.Language("nl")
                ),
                PokemonSpeciesDetailsDto.FlavorTextEntry(
                    "good morning",
                    PokemonSpeciesDetailsDto.FlavorTextEntry.Language("en")
                ),
            ),
            PokemonSpeciesDetailsDto.EvolutionChain("url"),
            42,
        )
        val mapper = PokemonSpeciesDetailsDtoMapper()

        val mapped = mapper.map(dto)

        mapped shouldBe PokemonDetails(42L, "name", "good morning", 42, null)
    }
}
