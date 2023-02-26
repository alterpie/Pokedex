package com.yerastov.assignment.pokedex.feature.feed.di

import android.content.Context
import com.yerastov.assignment.pokedex.core.di.common.ComponentInjector
import com.yerastov.assignment.pokedex.core.pokemon.species.di.PokemonSpeciesCoreComponentInjector

object PokemonFeedComponentInjector : ComponentInjector<Context, PokemonFeedComponent>() {

    override fun create(dependency: Context): PokemonFeedComponent {
        return DaggerPokemonFeedComponent.factory().create(
            PokemonSpeciesCoreComponentInjector.get(dependency).pokemonSpeciesRepository
        )
    }
}
