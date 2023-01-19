package com.assignment.catawiki.pokemon.impl.remote

import com.assignment.catawiki.pokemon.impl.remote.model.PokemonSpeciesDetailsDto
import com.assignment.catawiki.pokemon.impl.remote.model.PokemonSpeciesFeedPaginationDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

internal class PokemonRemoteDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
) : PokemonRemoteDataSource {

    override suspend fun fetchPokemonPage(path: String): PokemonSpeciesFeedPaginationDto {
        return httpClient.get(path).body()
    }

    override suspend fun fetchPokemonDetails(id: Long): PokemonSpeciesDetailsDto {
        return httpClient.get("pokemon-species/$id").body()
    }
}
