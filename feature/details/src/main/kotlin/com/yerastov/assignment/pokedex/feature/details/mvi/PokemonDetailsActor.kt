package com.yerastov.assignment.pokedex.feature.details.mvi

import com.yerastov.assignment.pokedex.core.pokemon.species.domain.PokemonSpeciesRepository
import com.yerastov.assignment.pokedex.core.pokemon.species.domain.error.GetSpeciesDetailsError
import com.yerastov.assignment.pokedex.core.pokemon.species.domain.error.GetSpeciesEvolutionError
import com.yerastov.assignment.pokedex.feature.details.mvi.PokemonDetailsContract.Effect
import com.yerastov.assignment.pokedex.feature.details.mvi.PokemonDetailsContract.Event
import com.yerastov.assignment.pokedex.mvi.Actor
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PokemonDetailsActor @Inject constructor(
    private val pokemonSpeciesRepository: PokemonSpeciesRepository,
) : Actor<Event, Effect> {

    override fun invoke(event: Event): Flow<Effect> = when (event) {
        is Event.GetPokemonSpecies -> pokemonSpeciesRepository.getPokemonSpecies(event.id)
            .map(Effect::DisplayPokemonDetails)
        is Event.GetPokemonDetails -> flow {
            emit(Effect.DisplayLoadingDetails)
            pokemonSpeciesRepository.getSpeciesDetails(event.id)
                .onFailure { throwable ->
                    when (throwable) {
                        is GetSpeciesDetailsError -> emit(Effect.DisplayLoadingDetailsFailed(event.id))
                        is GetSpeciesEvolutionError -> emit(
                            Effect.DisplayLoadingEvolutionFailed(event.id)
                        )
                    }
                }
        }
        is Event.GetPokemonEvolution -> flow {
            emit(Effect.DisplayLoadingEvolution)
            pokemonSpeciesRepository.getSpeciesEvolution(event.id)
                .onFailure { emit(Effect.DisplayLoadingEvolutionFailed(event.id)) }
        }
    }
}
