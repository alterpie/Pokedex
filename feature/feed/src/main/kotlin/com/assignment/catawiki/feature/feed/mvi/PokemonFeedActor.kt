package com.assignment.catawiki.feature.feed.mvi

import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.Effect
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.Event
import com.assignment.catawiki.mvi.Actor
import com.assignment.catawiki.pokemon.species.api.PokemonSpeciesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PokemonFeedActor @Inject constructor(
    private val pokemonSpeciesRepository: PokemonSpeciesRepository,
) : Actor<Event, Effect> {

    override fun invoke(event: Event): Flow<Effect> = when (event) {
        Event.GetPokemonFeed -> pokemonSpeciesRepository.getPokemonFeed()
            .map(Effect::DisplayPokemonFeed)
        Event.GetPokemonFeedNextPage -> flow {
            pokemonSpeciesRepository.getNextPokemonPage()
                .onFailure { Effect.DisplayLoadingFailure }
        }
    }
}
