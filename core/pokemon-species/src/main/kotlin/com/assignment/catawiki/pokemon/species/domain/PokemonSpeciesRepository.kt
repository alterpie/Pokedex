package com.assignment.catawiki.pokemon.species.domain

import com.assignment.catawiki.pokemon.species.domain.model.PokemonDetails
import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpeciesFeedItem
import kotlinx.coroutines.flow.Flow

interface PokemonSpeciesRepository {

    fun getPokemonFeed(): Flow<List<PokemonSpeciesFeedItem>>
    fun getPokemonDetails(id: Long): Flow<Result<PokemonDetails>>
    suspend fun getNextPokemonPage(): Result<Unit>
}
