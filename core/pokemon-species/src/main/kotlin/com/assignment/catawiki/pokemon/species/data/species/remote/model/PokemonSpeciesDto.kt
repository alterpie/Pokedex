package com.assignment.catawiki.pokemon.species.data.species.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PokemonSpeciesDto(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String,
)
