package com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.model.SpeciesEntity
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.model.UpdateCaptureRateDifference
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.model.UpdateSpeciesDetails
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.local.model.UpdateSpeciesEvolution
import kotlinx.coroutines.flow.Flow

@Dao
internal interface SpeciesDao {

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

    @Update(entity = SpeciesEntity::class)
    suspend fun updateCaptureDifferenceRate(updateCaptureRateDifference: UpdateCaptureRateDifference)

    @Query("DELETE FROM species")
    suspend fun deleteAll()

    @Query("SELECT COUNT(id) from species")
    suspend fun count(): Long
}
