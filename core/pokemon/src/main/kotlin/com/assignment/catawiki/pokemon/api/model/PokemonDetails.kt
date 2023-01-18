package com.assignment.catawiki.pokemon.api.model

data class PokemonDetails(
    val id: Long,
    val name: String,
    val description: String,
    val captureRate: Int,
    val evolution: Evolution?,
) {
    data class Evolution(
        val name: String,
        val imageUrl: String,
    )
}
