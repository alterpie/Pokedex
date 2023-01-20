package com.assignment.catawiki.pokemon.species.data.species.local

import com.assignment.catawiki.pokemon.species.data.species.local.model.SpeciesEntity
import com.assignment.catawiki.pokemon.species.data.species.local.model.UpdateSpeciesDetails
import kotlinx.coroutines.flow.Flow

interface PokemonSpeciesLocalDataSource {

    fun getSpecies(): Flow<List<SpeciesEntity>>
    suspend fun save(species: List<SpeciesEntity>)
    suspend fun updateDetails(updateSpeciesDetails: UpdateSpeciesDetails)
}
