package com.assignment.catawiki.pokemon.species.impl.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class EvolutionChainDto(
    @SerialName("chain")
    val chain: Chain,
) {

    @Serializable
    data class Chain(
        @SerialName("evolves_to")
        val evolutionTargets: List<EvolutionTarget>,
        @SerialName("species")
        val species: Species,
    ) {
        @Serializable
        data class EvolutionTarget(
            @SerialName("evolves_to")
            val evolutionTargets: List<EvolutionTarget>,
            @SerialName("species")
            val species: Species,
        )

        @Serializable
        data class Species(
            @SerialName("name")
            val name: String,
            @SerialName("url")
            val url: String,
        )
    }
}
