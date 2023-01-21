package com.assignment.catawiki.pokemon.species.data.species.local

import com.assignment.catawiki.pokemon.species.data.species.local.model.SpeciesEntity
import com.assignment.catawiki.pokemon.species.data.species.local.model.UpdateSpeciesDetails
import com.assignment.catawiki.pokemon.species.data.species.local.model.UpdateSpeciesEvolution
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class PokemonSpeciesLocalDataSourceImpl @Inject constructor(
    private val speciesDao: SpeciesDao,
) : PokemonSpeciesLocalDataSource {

    override fun getAllSpecies(): Flow<List<SpeciesEntity>> {
        return speciesDao.getAllSpecies()
    }

    override fun getSpecies(id: Long): Flow<SpeciesEntity> {
        return speciesDao.getSpecies(id)
    }

    override suspend fun save(species: List<SpeciesEntity>) {
        speciesDao.save(species)
    }

    override suspend fun updateDetails(updateSpeciesDetails: UpdateSpeciesDetails) {
        speciesDao.updateDetails(updateSpeciesDetails)
    }

    override suspend fun updateEvolution(updateSpeciesEvolution: UpdateSpeciesEvolution) {
        speciesDao.updateEvolution(updateSpeciesEvolution)
    }
}
