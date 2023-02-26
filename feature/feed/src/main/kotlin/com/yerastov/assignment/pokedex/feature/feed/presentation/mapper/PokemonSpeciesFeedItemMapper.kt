package com.yerastov.assignment.pokedex.feature.feed.presentation.mapper

import com.yerastov.assignment.pokedex.core.pokemon.species.domain.model.PokemonSpecies
import com.yerastov.assignment.pokedex.feature.feed.presentation.model.PokemonSpeciesFeedItem
import javax.inject.Inject

class PokemonSpeciesFeedItemMapper @Inject constructor() {

    fun map(from: PokemonSpecies): PokemonSpeciesFeedItem = with(from) {
        PokemonSpeciesFeedItem(id, name, imageUrl)
    }
}
