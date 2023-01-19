package com.assignment.catawiki.pokemon.species.api

import com.assignment.catawiki.pokemon.species.api.model.PokemonDetails
import com.assignment.catawiki.pokemon.species.api.model.PokemonSpeciesFeedItem
import kotlinx.coroutines.flow.Flow

interface PokemonSpeciesRepository {

    fun getPokemonFeed(): Flow<Result<List<PokemonSpeciesFeedItem>>>
    fun getPokemonDetails(id: Long): Flow<Result<PokemonDetails>>
    suspend fun getNextPokemonPage(): Result<Unit>
}
