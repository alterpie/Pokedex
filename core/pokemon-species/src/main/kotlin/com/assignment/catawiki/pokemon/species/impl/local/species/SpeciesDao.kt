package com.assignment.catawiki.pokemon.species.impl.local.species

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.assignment.catawiki.pokemon.species.impl.local.species.model.SpeciesEntity
import com.assignment.catawiki.pokemon.species.impl.local.species.model.UpdateSpeciesDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeciesDao {

    @Query("SELECT * FROM species")
    fun getSpecies(): Flow<List<SpeciesEntity>>

    @Upsert
    fun save(species: List<SpeciesEntity>)

    @Update(entity = SpeciesEntity::class)
    fun updateDetails(updateSpeciesDetails: UpdateSpeciesDetails)
}
