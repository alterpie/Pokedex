package com.assignment.catawiki.pokemon.species.data.species.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.assignment.catawiki.pokemon.species.data.species.local.species.SpeciesDao
import com.assignment.catawiki.pokemon.species.data.species.local.species.model.SpeciesEntity

@Database(entities = [SpeciesEntity::class], version = 1)
abstract class SpeciesDatabase : RoomDatabase() {

    abstract fun speciesDao(): SpeciesDao
}
