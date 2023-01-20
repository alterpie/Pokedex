package com.assignment.catawiki.pokemon.species.impl.mapper

import android.net.Uri
import com.assignment.catawiki.pokemon.species.BuildConfig
import com.assignment.catawiki.pokemon.species.data.species.mapper.PokemonSpeciesFeedItemDtoMapper
import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpeciesFeedItem
import com.assignment.catawiki.pokemon.species.data.species.remote.model.PokemonSpeciesFeedItemDto
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.Test

internal class PokemonSpeciesFeedItemDtoMapperTest {

    @Test
    fun `should map pokemon feed item`() {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk {
            every { pathSegments } returns listOf("api", "42")
        }
        val dto = PokemonSpeciesFeedItemDto("name", "https://host.com/api/42/")
        val mapper = PokemonSpeciesFeedItemDtoMapper()

        val mapped = mapper.map(dto)

        mapped shouldBe PokemonSpeciesFeedItem(
            42L,
            "name",
            "${BuildConfig.POKEMON_IMAGE_URL}42.png"
        )
        unmockkStatic(Uri::class)
    }
}
