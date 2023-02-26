package com.yerastov.assignment.pokedex.feature.feed.di

import com.yerastov.assignment.pokedex.core.pokemon.species.domain.PokemonSpeciesRepository
import com.yerastov.assignment.pokedex.feature.feed.presentation.PokemonFeedViewModelFactory
import dagger.BindsInstance
import dagger.Component

@Component
interface PokemonFeedComponent {

    val pokemonFeedViewModelFactory: PokemonFeedViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance pokemonSpeciesRepository: PokemonSpeciesRepository,
        ): PokemonFeedComponent
    }
}
