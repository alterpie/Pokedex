package com.assignment.catawiki.pokemon.species.impl.remote.model

import com.assignment.catawiki.pokemon.species.impl.remote.model.PokemonSpeciesFeedItemDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PokemonSpeciesFeedPaginationDto(
    @SerialName("count")
    val count: Long,
    @SerialName("next")
    val next: String?,
    @SerialName("results")
    val results: List<PokemonSpeciesFeedItemDto>,
)
