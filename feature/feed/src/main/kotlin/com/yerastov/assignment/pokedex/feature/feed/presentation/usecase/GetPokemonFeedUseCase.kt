package com.yerastov.assignment.pokedex.feature.feed.presentation.usecase

import com.yerastov.assignment.pokedex.core.pokemon.species.domain.PokemonSpeciesRepository
import com.yerastov.assignment.pokedex.feature.feed.presentation.mapper.PokemonSpeciesFeedItemMapper
import com.yerastov.assignment.pokedex.feature.feed.presentation.model.PokemonSpeciesFeedItem
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPokemonFeedUseCase @Inject constructor(
    private val pokemonSpeciesRepository: PokemonSpeciesRepository,
    private val pokemonSpeciesFeedItemMapper: PokemonSpeciesFeedItemMapper,
) {

    fun execute(): Flow<List<PokemonSpeciesFeedItem>> {
        return pokemonSpeciesRepository.getAllPokemonSpecies()
            .map { species -> species.map(pokemonSpeciesFeedItemMapper::map) }
    }
}
