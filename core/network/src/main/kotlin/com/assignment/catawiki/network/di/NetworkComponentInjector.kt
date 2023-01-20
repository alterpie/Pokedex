package com.assignment.catawiki.network.di

import com.assignment.catawiki.di.common.ComponentInjector

object NetworkComponentInjector : ComponentInjector<Unit, NetworkComponent>() {

    override fun create(dependency: Unit): NetworkComponent {
        return DaggerNetworkComponent.factory().create()
    }
}
