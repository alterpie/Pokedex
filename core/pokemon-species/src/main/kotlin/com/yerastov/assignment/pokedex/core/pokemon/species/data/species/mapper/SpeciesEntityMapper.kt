package com.yerastov.assignment.pokedex.core.pokemon.species.data.species.mapper

import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.model.SpeciesEntity
import com.yerastov.assignment.pokedex.core.pokemon.species.domain.model.PokemonSpecies
import javax.inject.Inject

internal class SpeciesEntityMapper @Inject constructor(){

    fun map(from: SpeciesEntity): PokemonSpecies = with(from) {
        PokemonSpecies(
            id,
            name,
            imageUrl,
            description,
            captureRateDifference,
            evolution?.let(::mapEvolution),
        )
    }

    private fun mapEvolution(from: SpeciesEntity.Evolution): PokemonSpecies.Evolution {
        return when (from) {
            SpeciesEntity.Evolution.Final -> PokemonSpecies.Evolution.Final
            is SpeciesEntity.Evolution.EvolvesTo -> PokemonSpecies.Evolution.EvolvesTo(
                from.name,
                from.imageUrl
            )
        }
    }
}
