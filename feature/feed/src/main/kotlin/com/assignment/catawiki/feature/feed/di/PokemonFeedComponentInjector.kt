package com.assignment.catawiki.feature.feed.di

import android.content.Context
import com.assignment.catawiki.di.common.ComponentInjector
import com.assignment.catawiki.pokemon.species.di.PokemonSpeciesCoreComponentInjector

object PokemonFeedComponentInjector : ComponentInjector<Context, PokemonFeedComponent>() {

    override fun create(dependency: Context): PokemonFeedComponent {
        return DaggerPokemonFeedComponent.factory().create(
            PokemonSpeciesCoreComponentInjector.get(dependency).pokemonSpeciesRepository
        )
    }
}
