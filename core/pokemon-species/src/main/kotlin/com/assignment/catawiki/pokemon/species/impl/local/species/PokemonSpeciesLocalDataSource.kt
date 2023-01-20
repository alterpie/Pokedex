package com.assignment.catawiki.pokemon.species.impl.local.species

import com.assignment.catawiki.pokemon.species.impl.local.species.model.SpeciesEntity
import com.assignment.catawiki.pokemon.species.impl.local.species.model.UpdateSpeciesDetails
import kotlinx.coroutines.flow.Flow

interface PokemonSpeciesLocalDataSource {

    fun getSpecies(): Flow<List<SpeciesEntity>>
    suspend fun save(species: List<SpeciesEntity>)
    suspend fun updateDetails(updateSpeciesDetails: UpdateSpeciesDetails)
}
