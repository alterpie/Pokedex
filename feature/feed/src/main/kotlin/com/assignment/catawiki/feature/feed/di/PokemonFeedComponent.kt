package com.assignment.catawiki.feature.feed.di

import com.assignment.catawiki.feature.feed.PokemonFeedViewModelFactory
import com.assignment.catawiki.pokemon.species.domain.PokemonSpeciesRepository
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
