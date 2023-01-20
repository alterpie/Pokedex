package com.assignment.catawiki.pokemon.species.data.species.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PokemonSpeciesDetailsDto(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry>,
    @SerialName("evolution_chain")
    val evolutionChain: EvolutionChain,
    @SerialName("capture_rate")
    val captureRate: Int,
) {
    @Serializable
    data class FlavorTextEntry(
        @SerialName("flavor_text")
        val text: String,
        @SerialName("language")
        val language: Language,
    ) {
        @Serializable
        data class Language(
            @SerialName("name")
            val name: String
        )
    }

    @Serializable
    data class EvolutionChain(
        @SerialName("url") val url: String,
    )
}
