package com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.converter

import androidx.room.TypeConverter
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.model.SpeciesEntity

internal class SpeciesEvolutionTypeConverter {

    @TypeConverter
    fun fromString(value: String): SpeciesEntity.Evolution {
        return when (value) {
            "final" -> SpeciesEntity.Evolution.Final
            else -> {
                val splits = value.split(" ")
                SpeciesEntity.Evolution.EvolvesTo(splits[0].toLong(), splits[1], splits[2])
            }
        }
    }

    @TypeConverter
    fun fromEvolution(evolution: SpeciesEntity.Evolution): String {
        return when (evolution) {
            SpeciesEntity.Evolution.Final -> "final"
            is SpeciesEntity.Evolution.EvolvesTo -> "${evolution.pokemonId} ${evolution.name} ${evolution.imageUrl}"
        }
    }
}
