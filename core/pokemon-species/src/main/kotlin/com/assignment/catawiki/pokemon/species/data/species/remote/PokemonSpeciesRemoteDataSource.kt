package com.assignment.catawiki.pokemon.species.data.species.remote

import com.assignment.catawiki.pokemon.species.data.species.remote.model.EvolutionChainDto
import com.assignment.catawiki.pokemon.species.data.species.remote.model.PokemonSpeciesDetailsDto
import com.assignment.catawiki.pokemon.species.data.species.remote.model.PokemonSpeciesFeedPaginationDto

internal interface PokemonSpeciesRemoteDataSource {
    suspend fun fetchPokemonPage(path: String): PokemonSpeciesFeedPaginationDto
    suspend fun fetchPokemonDetails(id: Long): PokemonSpeciesDetailsDto

    suspend fun fetchEvolutionChain(path: String): EvolutionChainDto
}
