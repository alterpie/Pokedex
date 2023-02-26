package com.yerastov.assignment.pokedex.feature.details.di

import android.content.Context
import com.yerastov.assignment.pokedex.core.di.common.ComponentInjector
import com.yerastov.assignment.pokedex.core.pokemon.species.di.PokemonSpeciesCoreComponentInjector

object PokemonDetailsComponentInjector : ComponentInjector<Context, PokemonDetailsComponent>(){

    override fun create(dependency: Context): PokemonDetailsComponent {
        return DaggerPokemonDetailsComponent.factory().create(
            PokemonSpeciesCoreComponentInjector.get(dependency).pokemonSpeciesRepository,
        )
    }
}
