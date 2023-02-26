package com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.converter.SpeciesEvolutionTypeConverter
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.model.SpeciesEntity

@Database(entities = [SpeciesEntity::class], version = 1)
@TypeConverters(value = [SpeciesEvolutionTypeConverter::class])
internal abstract class SpeciesDatabase : RoomDatabase() {

    abstract fun speciesDao(): SpeciesDao
}
