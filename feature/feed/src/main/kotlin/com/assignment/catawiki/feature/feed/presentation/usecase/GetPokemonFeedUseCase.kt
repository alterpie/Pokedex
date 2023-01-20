package com.assignment.catawiki.feature.feed.presentation.usecase

import com.assignment.catawiki.feature.feed.presentation.mapper.PokemonSpeciesFeedItemMapper
import com.assignment.catawiki.feature.feed.presentation.model.PokemonSpeciesFeedItem
import com.assignment.catawiki.pokemon.species.domain.PokemonSpeciesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPokemonFeedUseCase @Inject constructor(
    private val pokemonSpeciesRepository: PokemonSpeciesRepository,
    private val pokemonSpeciesFeedItemMapper: PokemonSpeciesFeedItemMapper,
) {

    fun execute(): Flow<List<PokemonSpeciesFeedItem>> {
        return pokemonSpeciesRepository.getAllPokemonSpecies()
            .map { species -> species.map(pokemonSpeciesFeedItemMapper::map) }
    }
}
