package com.assignment.catawiki.pokemon.species.data.species.mapper

import com.assignment.catawiki.pokemon.species.data.species.local.model.SpeciesEntity
import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpecies
import javax.inject.Inject

internal class SpeciesEntityMapper @Inject constructor(){

    fun map(from: SpeciesEntity): PokemonSpecies = with(from) {
        PokemonSpecies(
            id,
            name,
            imageUrl,
            description,
            captureRate,
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
