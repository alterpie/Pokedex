package com.yerastov.assignment.pokedex.feature.details.di

import com.yerastov.assignment.pokedex.core.pokemon.species.domain.PokemonSpeciesRepository
import com.yerastov.assignment.pokedex.feature.details.presentation.PokemonDetailsViewModelFactory
import dagger.BindsInstance
import dagger.Component

@Component
interface PokemonDetailsComponent {

    val pokemonDetailsViewModelFactory: PokemonDetailsViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance pokemonSpeciesRepository: PokemonSpeciesRepository): PokemonDetailsComponent
    }
}
