package com.assignment.catawiki.pokemon.species.data.species.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.assignment.catawiki.pokemon.species.data.species.local.model.SpeciesEntity
import com.assignment.catawiki.pokemon.species.data.species.local.model.UpdateSpeciesDetails
import com.assignment.catawiki.pokemon.species.data.species.local.model.UpdateSpeciesEvolution
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeciesDao {

    @Query("SELECT * FROM species")
    fun getAllSpecies(): Flow<List<SpeciesEntity>>

    @Query("SELECT * FROM species WHERE id =:id")
    fun getSpecies(id: Long): Flow<SpeciesEntity>

    @Upsert
    suspend fun save(species: List<SpeciesEntity>)

    @Update(entity = SpeciesEntity::class)
    suspend fun updateDetails(updateSpeciesDetails: UpdateSpeciesDetails)

    @Update(entity = SpeciesEntity::class)
    suspend fun updateEvolution(updateSpeciesEvolution: UpdateSpeciesEvolution)
}
