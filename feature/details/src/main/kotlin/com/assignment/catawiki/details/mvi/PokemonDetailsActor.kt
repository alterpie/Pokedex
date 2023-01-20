package com.assignment.catawiki.details.mvi

import com.assignment.catawiki.details.mvi.PokemonDetailsContract.Effect
import com.assignment.catawiki.details.mvi.PokemonDetailsContract.Event
import com.assignment.catawiki.mvi.Actor
import com.assignment.catawiki.pokemon.species.domain.PokemonSpeciesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PokemonDetailsActor @Inject constructor(
    private val pokemonSpeciesRepository: PokemonSpeciesRepository,
) : Actor<Event, Effect> {

    override fun invoke(event: Event): Flow<Effect> = when (event) {
        is Event.GetPokemonDetails -> pokemonSpeciesRepository.getPokemonDetails(event.id)
            .map { result ->
                result.fold(
                    onSuccess = { Effect.DisplayPokemonDetails(it) },
                    onFailure = { Effect.DisplayError }
                )
            }
    }
}
