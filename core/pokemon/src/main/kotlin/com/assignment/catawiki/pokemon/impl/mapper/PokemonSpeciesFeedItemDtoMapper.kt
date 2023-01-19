package com.assignment.catawiki.pokemon.impl.mapper

import android.net.Uri
import com.assignment.catawiki.pokemon.BuildConfig
import com.assignment.catawiki.pokemon.api.model.PokemonSpeciesFeedItem
import com.assignment.catawiki.pokemon.impl.remote.model.PokemonSpeciesFeedItemDto
import javax.inject.Inject

internal class PokemonSpeciesFeedItemDtoMapper @Inject constructor() {

    fun map(from: PokemonSpeciesFeedItemDto): PokemonSpeciesFeedItem {
        return with(from) {
            val id = Uri.parse(url).pathSegments.last().toLong()
            PokemonSpeciesFeedItem(id, name, "${BuildConfig.POKEMON_IMAGE_URL}$id.png")
        }
    }
}
