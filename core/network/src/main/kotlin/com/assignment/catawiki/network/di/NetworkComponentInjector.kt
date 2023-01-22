package com.assignment.catawiki.network.di

import android.content.Context
import com.assignment.catawiki.di.common.ComponentInjector

object NetworkComponentInjector : ComponentInjector<Context, NetworkComponent>() {

    override fun create(dependency: Context): NetworkComponent {
        return DaggerNetworkComponent.factory().create(dependency)
    }
}
