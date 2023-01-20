package com.assignment.catawiki.pokemon.species.impl.local

import com.assignment.catawiki.pokemon.species.impl.local.species.PokemonSpeciesLocalDataSource
import com.assignment.catawiki.pokemon.species.impl.local.species.SpeciesDao
import com.assignment.catawiki.pokemon.species.impl.local.species.model.SpeciesEntity
import com.assignment.catawiki.pokemon.species.impl.local.species.model.UpdateSpeciesDetails
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class PokemonSpeciesLocalDataSourceImpl @Inject constructor(
    private val speciesDao: SpeciesDao
) : PokemonSpeciesLocalDataSource {

    override fun getSpecies(): Flow<List<SpeciesEntity>> {
        return speciesDao.getSpecies()
    }

    override suspend fun save(species: List<SpeciesEntity>) {
        speciesDao.save(species)
    }

    override suspend fun updateDetails(updateSpeciesDetails: UpdateSpeciesDetails) {
        speciesDao.updateDetails(updateSpeciesDetails)
    }
}
