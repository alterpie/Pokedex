package com.assignment.catawiki.pokemon.species.data.species.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.assignment.catawiki.pokemon.species.data.species.local.converter.SpeciesEvolutionTypeConverter
import com.assignment.catawiki.pokemon.species.data.species.local.model.SpeciesEntity

@Database(entities = [SpeciesEntity::class], version = 1)
@TypeConverters(value = [SpeciesEvolutionTypeConverter::class])
internal abstract class SpeciesDatabase : RoomDatabase() {

    abstract fun speciesDao(): SpeciesDao
}
