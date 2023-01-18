package com.assignment.catawiki.pokemon.impl.remote

import com.assignment.catawiki.pokemon.impl.remote.model.PokemonSpeciesDetailsDto
import com.assignment.catawiki.pokemon.impl.remote.model.PokemonSpeciesFeedPaginationDto

internal interface PokemonRemoteDataSource {
    suspend fun fetchPokemonPage(page: Int): PokemonSpeciesFeedPaginationDto
    suspend fun fetchPokemonDetails(id: Long): PokemonSpeciesDetailsDto
}
