package com.assignment.catawiki.pokemon.species.domain.model

data class PokemonDetails(
    val id: Long,
    val name: String,
    val description: String,
    val captureRate: Int,
    val evolution: Evolution?,
) {
    sealed interface Evolution {
        data class Next(
            val name: String,
            val imageUrl: String,
        ) : Evolution

        object Final : Evolution
    }
}
