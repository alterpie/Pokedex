package com.assignment.catawiki.pokemon.species.data.species.local.converter

import com.assignment.catawiki.pokemon.species.data.species.local.model.SpeciesEntity
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SpeciesEvolutionTypeConverterTest {

    @Test
    fun `should map from class of final evolution to string representation`() {
        val converter = SpeciesEvolutionTypeConverter()
        val evolution = SpeciesEntity.Evolution.Final

        val actual = converter.fromEvolution(evolution)

        actual shouldBe "final"
    }

    @Test
    fun `should map from class of next evolution link to string representation`() {
        val converter = SpeciesEvolutionTypeConverter()
        val evolution = SpeciesEntity.Evolution.EvolvesTo("name", "image")

        val actual = converter.fromEvolution(evolution)

        actual shouldBe "name image"
    }

    @Test
    fun `should map from string to class of next evolution link model`() {
        val converter = SpeciesEvolutionTypeConverter()
        val evolutionRaw = "name imageUrl"

        val actual = converter.fromString(evolutionRaw)

        actual shouldBe SpeciesEntity.Evolution.EvolvesTo("name", "imageUrl")
    }

    @Test
    fun `should map from string to class of final evolution model`() {
        val converter = SpeciesEvolutionTypeConverter()
        val evolutionRaw = "final"

        val actual = converter.fromString(evolutionRaw)

        actual shouldBe SpeciesEntity.Evolution.Final
    }
}
