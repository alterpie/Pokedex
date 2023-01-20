package com.assignment.catawiki.pokemon.species.domain

import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpecies
import kotlinx.coroutines.flow.Flow

interface PokemonSpeciesRepository {

    fun getAllPokemonSpecies(): Flow<List<PokemonSpecies>>
    fun getPokemonSpecies(id: Long): Flow<Result<PokemonSpecies>>
    suspend fun getNextPokemonPage(): Result<Unit>
}
