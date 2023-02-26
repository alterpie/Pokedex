package com.yerastov.assignment.pokedex.core.network.di

import android.content.Context
import com.yerastov.assignment.pokedex.core.di.common.ComponentInjector

object NetworkComponentInjector : ComponentInjector<Context, NetworkComponent>() {

    override fun create(dependency: Context): NetworkComponent {
        return DaggerNetworkComponent.factory().create(dependency)
    }
}
