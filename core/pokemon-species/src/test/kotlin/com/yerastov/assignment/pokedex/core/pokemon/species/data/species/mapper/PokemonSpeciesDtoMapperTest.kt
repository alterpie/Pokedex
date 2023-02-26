package com.yerastov.assignment.pokedex.core.pokemon.species.data.species.mapper

import android.net.Uri
import com.yerastov.assignment.pokedex.core.pokemon.species.BuildConfig
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.model.SpeciesEntity
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.remote.model.PokemonSpeciesDto
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.Test

internal class PokemonSpeciesDtoMapperTest {

    @Test
    fun `should map pokemon feed item`() {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk {
            every { pathSegments } returns listOf("api", "42")
        }
        val dto = PokemonSpeciesDto("name", "https://host.com/api/42/")
        val mapper = PokemonSpeciesDtoMapper()

        val mapped = mapper.map(dto)

        mapped shouldBe SpeciesEntity(
            42L,
            "name",
            "${BuildConfig.POKEMON_IMAGE_URL}42.png",
            null,
            null,
            null,
            null,
            null,
        )
        unmockkStatic(Uri::class)
    }
}
