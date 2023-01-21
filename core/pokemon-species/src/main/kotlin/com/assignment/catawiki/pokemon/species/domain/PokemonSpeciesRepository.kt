package com.assignment.catawiki.pokemon.species.domain

import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpecies
import kotlinx.coroutines.flow.Flow

interface PokemonSpeciesRepository {

    fun getAllPokemonSpecies(): Flow<List<PokemonSpecies>>
    fun getPokemonSpecies(id: Long): Flow<PokemonSpecies>
    suspend fun getSpeciesDetails(id: Long): Result<Unit>
    suspend fun getSpeciesEvolution(id: Long): Result<Unit>
    suspend fun getNextPokemonPage(): Result<Unit>
}
