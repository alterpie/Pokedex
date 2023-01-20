package com.assignment.catawiki.details.di

import com.assignment.catawiki.details.presentation.PokemonDetailsViewModelFactory
import com.assignment.catawiki.pokemon.species.domain.PokemonSpeciesRepository
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
