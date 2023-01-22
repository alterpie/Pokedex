package com.assignment.catawiki.pokemon.species.data.species.local

import com.assignment.catawiki.pokemon.species.data.species.local.model.SpeciesEntity
import com.assignment.catawiki.pokemon.species.data.species.local.model.UpdateSpeciesDetails
import com.assignment.catawiki.pokemon.species.data.species.local.model.UpdateSpeciesEvolution
import kotlinx.coroutines.flow.Flow

internal interface PokemonSpeciesLocalDataSource {

    fun getAllSpecies(): Flow<List<SpeciesEntity>>
    fun getSpecies(id: Long): Flow<SpeciesEntity>
    suspend fun save(species: List<SpeciesEntity>)
    suspend fun updateDetails(updateSpeciesDetails: UpdateSpeciesDetails)
    suspend fun updateEvolution(updateSpeciesEvolution: UpdateSpeciesEvolution)

    suspend fun removeAll()

    suspend fun getCount(): Long
}
