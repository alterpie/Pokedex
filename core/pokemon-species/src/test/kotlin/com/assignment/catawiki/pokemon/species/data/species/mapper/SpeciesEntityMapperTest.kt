package com.assignment.catawiki.pokemon.species.data.species.mapper

import com.assignment.catawiki.pokemon.species.data.species.local.model.SpeciesEntity
import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpecies
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SpeciesEntityMapperTest {

    @Test
    fun `should map to domain model with final evolution`() {
        val speciesEntity = SpeciesEntity(
            42L,
            "charmander",
            "image url",
            "description",
            42,
            "evolution chain url",
            SpeciesEntity.Evolution.Final,
            24,
        )
        val mapper = SpeciesEntityMapper()

        val mapped = mapper.map(speciesEntity)

        mapped shouldBe PokemonSpecies(
            42L,
            "charmander",
            "image url",
            "description",
            24,
            PokemonSpecies.Evolution.Final,
        )
    }

    @Test
    fun `should map to domain model with next evolution chain`() {
        val speciesEntity = SpeciesEntity(
            42L,
            "charmander",
            "image url",
            "description",
            42,
            "evolution chain url",
            SpeciesEntity.Evolution.EvolvesTo(43, "charmeleon", "other image url"),
            24,
        )
        val mapper = SpeciesEntityMapper()

        val mapped = mapper.map(speciesEntity)

        mapped shouldBe PokemonSpecies(
            42L,
            "charmander",
            "image url",
            "description",
            24,
            PokemonSpecies.Evolution.EvolvesTo("charmeleon", "other image url"),
        )
    }
}
