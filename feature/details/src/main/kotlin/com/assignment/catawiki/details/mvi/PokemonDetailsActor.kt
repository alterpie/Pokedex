package com.assignment.catawiki.details.mvi

import com.assignment.catawiki.details.mvi.PokemonDetailsContract.Effect
import com.assignment.catawiki.details.mvi.PokemonDetailsContract.Event
import com.assignment.catawiki.mvi.Actor
import com.assignment.catawiki.pokemon.species.domain.PokemonSpeciesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PokemonDetailsActor @Inject constructor(
    private val pokemonSpeciesRepository: PokemonSpeciesRepository,
) : Actor<Event, Effect> {

    override fun invoke(event: Event): Flow<Effect> = when (event) {
        is Event.GetPokemonSpecies -> pokemonSpeciesRepository.getPokemonSpecies(event.id)
            .map(Effect::DisplayPokemonDetails)
        is Event.GetPokemonDetails -> flow {
            pokemonSpeciesRepository.getSpeciesDetails(event.id)
        }
    }
}
