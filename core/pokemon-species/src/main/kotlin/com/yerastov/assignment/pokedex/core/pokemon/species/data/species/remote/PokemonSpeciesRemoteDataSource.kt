package com.yerastov.assignment.pokedex.core.pokemon.species.data.species.remote

import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.remote.model.EvolutionChainDto
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.remote.model.PokemonSpeciesDetailsDto
import com.yerastov.assignment.pokedex.core.pokemon.species.data.species.remote.model.PokemonSpeciesFeedPaginationDto

internal interface PokemonSpeciesRemoteDataSource {
    suspend fun fetchPokemonPage(path: String): PokemonSpeciesFeedPaginationDto
    suspend fun fetchPokemonDetails(id: Long): PokemonSpeciesDetailsDto

    suspend fun fetchEvolutionChain(path: String): EvolutionChainDto
}
