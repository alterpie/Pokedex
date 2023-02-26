package com.yerastov.assignment.pokedex.core.pokemon.species.di

import android.content.Context
import com.yerastov.assignment.pokedex.core.di.common.ComponentInjector
import com.yerastov.assignment.pokedex.core.network.di.NetworkComponentInjector

object PokemonSpeciesCoreComponentInjector :
    ComponentInjector<Context, PokemonSpeciesCoreComponent>() {

    override fun create(dependency: Context): PokemonSpeciesCoreComponent {
        return DaggerPokemonSpeciesCoreComponent.factory()
            .create(
                dependency,
                NetworkComponentInjector.get(dependency).httpClient,
            )
    }
}
