package com.assignment.catawiki.pokemon.species.di

import android.content.Context
import com.assignment.catawiki.di.common.ComponentInjector
import com.assignment.catawiki.network.di.NetworkComponentInjector

object PokemonSpeciesCoreComponentInjector : ComponentInjector<Context, PokemonSpeciesCoreComponent>() {

    override fun create(dependency: Context): PokemonSpeciesCoreComponent {
        return DaggerPokemonSpeciesCoreComponent.factory().create(dependency, NetworkComponentInjector.get(Unit).httpClient)
    }
}
