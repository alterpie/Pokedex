package com.yerastov.assignment.pokedex.core.pokemon.species.domain.model

data class PokemonSpecies(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val description: String?,
    val captureRateDifference: Int?,
    val evolution: Evolution?,
) {
    sealed interface Evolution {
        data class EvolvesTo(
            val name: String,
            val imageUrl: String,
        ) : Evolution

        object Final : Evolution
    }
}
