package com.assignment.catawiki.details.di

import android.content.Context
import com.assignment.catawiki.di.common.ComponentInjector
import com.assignment.catawiki.pokemon.species.di.PokemonSpeciesCoreComponentInjector

object PokemonDetailsComponentInjector :ComponentInjector<Context, PokemonDetailsComponent>(){

    override fun create(dependency: Context): PokemonDetailsComponent {
        return DaggerPokemonDetailsComponent.factory().create(
            PokemonSpeciesCoreComponentInjector.get(dependency).pokemonSpeciesRepository,
        )
    }
}
