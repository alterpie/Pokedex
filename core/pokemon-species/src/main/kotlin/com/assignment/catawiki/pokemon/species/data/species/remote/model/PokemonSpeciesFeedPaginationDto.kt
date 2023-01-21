package com.assignment.catawiki.pokemon.species.data.species.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PokemonSpeciesFeedPaginationDto(
    @SerialName("count")
    val count: Long,
    @SerialName("next")
    val next: String?,
    @SerialName("results")
    val results: List<PokemonSpeciesDto>,
)
