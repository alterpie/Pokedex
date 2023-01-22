package com.assignment.catawiki.feature.feed.presentation.mapper

import com.assignment.catawiki.feature.feed.presentation.model.PokemonSpeciesFeedItem
import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpecies
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class PokemonSpeciesFeedItemMapperTest {

    @Test
    fun `should map domain model to feed item`() {
        val species = buildPokemonSpecies()
        val mapper = PokemonSpeciesFeedItemMapper()

        val mapped = mapper.map(species)

        mapped shouldBe PokemonSpeciesFeedItem(42L, "name", "image")
    }

    private fun buildPokemonSpecies(): PokemonSpecies {
        return PokemonSpecies(42L, "name", "image", "description", 42, null)
    }
}
