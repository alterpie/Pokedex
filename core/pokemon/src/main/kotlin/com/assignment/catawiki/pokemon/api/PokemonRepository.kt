package com.assignment.catawiki.pokemon.api

import com.assignment.catawiki.pokemon.api.model.PokemonDetails
import com.assignment.catawiki.pokemon.api.model.PokemonFeedItem
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun getPokemonFeed(): Flow<Result<List<PokemonFeedItem>>>
    fun getPokemonDetails(id: Long): Flow<Result<PokemonDetails>>
    suspend fun getNextPokemonPage()
}
