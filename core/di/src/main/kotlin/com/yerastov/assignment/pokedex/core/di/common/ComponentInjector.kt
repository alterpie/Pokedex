package com.yerastov.assignment.pokedex.core.di.common

abstract class ComponentInjector<Dependency, Component> {

    private var component: Component? = null

    open fun get(dependency: Dependency): Component {
        return component ?: create(dependency).also { component = it }
    }

    open fun clear() {
        component = null
    }

    abstract fun create(dependency: Dependency): Component

}
