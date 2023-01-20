package com.assignment.catawiki.pokemon.species.api.model

data class PokemonDetails(
    val id: Long,
    val name: String,
    val description: String,
    val captureRate: Int,
    val evolutionChain: EvolutionChain?,
) {
    data class EvolutionChain(
        val name: String,
        val imageUrl: String,
    )
}
