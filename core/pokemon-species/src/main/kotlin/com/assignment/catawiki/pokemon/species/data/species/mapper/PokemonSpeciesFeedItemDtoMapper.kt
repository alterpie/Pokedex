package com.assignment.catawiki.pokemon.species.data.species.mapper

import android.net.Uri
import com.assignment.catawiki.pokemon.species.BuildConfig
import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpeciesFeedItem
import com.assignment.catawiki.pokemon.species.data.species.remote.model.PokemonSpeciesFeedItemDto
import javax.inject.Inject

internal class PokemonSpeciesFeedItemDtoMapper @Inject constructor() {

    fun map(from: PokemonSpeciesFeedItemDto): PokemonSpeciesFeedItem {
        return with(from) {
            val id = Uri.parse(url).pathSegments.last().toLong()
            PokemonSpeciesFeedItem(id, name, "${BuildConfig.POKEMON_IMAGE_URL}$id.png")
        }
    }
}
