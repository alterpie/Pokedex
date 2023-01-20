package com.assignment.catawiki.pokemon.species.impl.remote

import com.assignment.catawiki.pokemon.species.impl.remote.model.EvolutionChainDto
import com.assignment.catawiki.pokemon.species.impl.remote.model.PokemonSpeciesDetailsDto
import com.assignment.catawiki.pokemon.species.impl.remote.model.PokemonSpeciesFeedPaginationDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

internal class PokemonSpeciesRemoteDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
) : PokemonSpeciesRemoteDataSource {

    override suspend fun fetchPokemonPage(path: String): PokemonSpeciesFeedPaginationDto {
        return httpClient.get(path).body()
    }

    override suspend fun fetchPokemonDetails(id: Long): PokemonSpeciesDetailsDto {
        return httpClient.get("pokemon-species/$id").body()
    }

    override suspend fun fetchEvolutionChain(path: String): EvolutionChainDto {
        return httpClient.get(path).body()
    }
}
