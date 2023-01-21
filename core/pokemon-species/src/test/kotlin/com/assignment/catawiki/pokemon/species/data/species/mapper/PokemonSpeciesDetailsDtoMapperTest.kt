package com.assignment.catawiki.pokemon.species.data.species.mapper

import com.assignment.catawiki.pokemon.species.data.species.local.model.UpdateSpeciesDetails
import com.assignment.catawiki.pokemon.species.data.species.mapper.PokemonSpeciesDetailsDtoMapper
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

        mapped shouldBe UpdateSpeciesDetails(
            42L,
            "good morning",
            42,
            "url",
        )
    }
}
