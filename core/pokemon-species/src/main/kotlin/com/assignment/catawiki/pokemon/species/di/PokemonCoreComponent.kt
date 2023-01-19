package com.assignment.catawiki.pokemon.species.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.assignment.catawiki.pokemon.species.api.PokemonSpeciesRepository
import com.assignment.catawiki.pokemon.species.impl.PokemonSpeciesRepositoryImpl
import com.assignment.catawiki.pokemon.species.impl.local.PokemonSpeciesFeedPaginationDataSource
import com.assignment.catawiki.pokemon.species.impl.local.model.PokemonSpeciesFeedPaginationDataSourceImpl
import com.assignment.catawiki.pokemon.species.impl.remote.PokemonSpeciesRemoteDataSource
import com.assignment.catawiki.pokemon.species.impl.remote.PokemonSpeciesRemoteDataSourceImpl
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Component(modules = [PokemonCoreModule::class])
@Singleton
interface PokemonCoreComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance dataStore: DataStore<Preferences>): PokemonCoreComponent
    }
}

@Module
private interface PokemonCoreModule {

    @Binds
    fun feedPaginationDataSource(impl: PokemonSpeciesFeedPaginationDataSourceImpl): PokemonSpeciesFeedPaginationDataSource

    @Binds
    fun remoteDataSource(impl: PokemonSpeciesRemoteDataSourceImpl): PokemonSpeciesRemoteDataSource

    @Binds
    fun repository(impl: PokemonSpeciesRepositoryImpl): PokemonSpeciesRepository
}
