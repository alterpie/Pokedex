package com.assignment.catawiki.pokemon.impl.remote

import com.assignment.catawiki.pokemon.impl.remote.model.PokemonSpeciesDetailsDto
import com.assignment.catawiki.pokemon.impl.remote.model.PokemonSpeciesFeedPaginationDto

internal interface PokemonSpeciesRemoteDataSource {
    suspend fun fetchPokemonPage(path: String): PokemonSpeciesFeedPaginationDto
    suspend fun fetchPokemonDetails(id: Long): PokemonSpeciesDetailsDto
}
