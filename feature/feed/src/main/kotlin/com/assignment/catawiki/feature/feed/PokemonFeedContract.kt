package com.assignment.catawiki.feature.feed

import com.assignment.catawiki.pokemon.species.api.model.PokemonSpeciesFeedItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal interface PokemonFeedContract {

    data class State(
        val items: ImmutableList<PokemonSpeciesFeedItem> = persistentListOf(),
    )
}
