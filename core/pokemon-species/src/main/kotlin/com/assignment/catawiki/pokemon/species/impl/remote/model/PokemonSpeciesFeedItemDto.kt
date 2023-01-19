package com.assignment.catawiki.pokemon.species.impl.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PokemonSpeciesFeedItemDto(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String,
)
