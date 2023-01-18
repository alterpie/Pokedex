package com.assignment.catawiki.pokemon.impl.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PokemonSpeciesFeedPaginationDto(
    @SerialName("count")
    val count: Int,
    @SerialName("next")
    val next: String,
    @SerialName("results")
    val results: List<PokemonSpeciesFeedItemDto>,
)
