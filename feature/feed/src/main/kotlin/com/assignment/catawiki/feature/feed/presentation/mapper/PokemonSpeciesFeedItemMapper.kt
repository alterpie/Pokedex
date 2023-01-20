package com.assignment.catawiki.feature.feed.presentation.mapper

import com.assignment.catawiki.feature.feed.presentation.model.PokemonSpeciesFeedItem
import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpecies
import javax.inject.Inject

class PokemonSpeciesFeedItemMapper @Inject constructor() {

    fun map(from: PokemonSpecies): PokemonSpeciesFeedItem = with(from) {
        PokemonSpeciesFeedItem(id, name, imageUrl)
    }
}
