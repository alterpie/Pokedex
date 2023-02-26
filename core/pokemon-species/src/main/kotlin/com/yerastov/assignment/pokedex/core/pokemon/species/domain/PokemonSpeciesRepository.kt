package com.yerastov.assignment.pokedex.core.pokemon.species.domain

import com.yerastov.assignment.pokedex.core.pokemon.species.domain.model.PokemonSpecies
import kotlinx.coroutines.flow.Flow

interface PokemonSpeciesRepository {

    fun getAllPokemonSpecies(): Flow<List<PokemonSpecies>>
    fun getPokemonSpecies(id: Long): Flow<PokemonSpecies>
    suspend fun getSpeciesDetails(id: Long): Result<Unit>
    suspend fun getSpeciesEvolution(id: Long): Result<Unit>
    suspend fun getNextSpeciesPage(refresh: Boolean): Result<Unit>

    suspend fun getStoredItemsCount(): Long
}
