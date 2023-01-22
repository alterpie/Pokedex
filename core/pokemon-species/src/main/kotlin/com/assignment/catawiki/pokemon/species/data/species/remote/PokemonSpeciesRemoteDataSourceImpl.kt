package com.assignment.catawiki.pokemon.species.data.species.remote

import com.assignment.catawiki.pokemon.species.data.species.remote.model.EvolutionChainDto
import com.assignment.catawiki.pokemon.species.data.species.remote.model.PokemonSpeciesDetailsDto
import com.assignment.catawiki.pokemon.species.data.species.remote.model.PokemonSpeciesFeedPaginationDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import kotlinx.coroutines.delay

internal class PokemonSpeciesRemoteDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
) : PokemonSpeciesRemoteDataSource {

    override suspend fun fetchPokemonPage(path: String): PokemonSpeciesFeedPaginationDto {
        delay(2000)
        return httpClient.get(path).body()
    }

    override suspend fun fetchPokemonDetails(id: Long): PokemonSpeciesDetailsDto {
        delay(2000)
        return httpClient.get("pokemon-species/$id").body()
    }

    override suspend fun fetchEvolutionChain(path: String): EvolutionChainDto {
        delay(2000)
        throw IllegalStateException("no pokemon evolution")
//        return httpClient.get(path).body()
    }
}
