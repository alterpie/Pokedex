package com.assignment.catawiki.pokemon.species.data.species.local.converter

import androidx.room.TypeConverter
import com.assignment.catawiki.pokemon.species.data.species.local.model.SpeciesEntity

class SpeciesEvolutionTypeConverter {

    @TypeConverter
    fun fromString(value: String): SpeciesEntity.Evolution {
        return when (value) {
            "final" -> SpeciesEntity.Evolution.Final
            else -> {
                val splits = value.split(" ")
                SpeciesEntity.Evolution.EvolvesTo(splits[0], splits[1])
            }
        }
    }

    @TypeConverter
    fun fromStringMap(evolution: SpeciesEntity.Evolution): String {
        return when (evolution) {
            SpeciesEntity.Evolution.Final -> "final"
            is SpeciesEntity.Evolution.EvolvesTo -> "${evolution.name} ${evolution.imageUrl}"
        }
    }
}
