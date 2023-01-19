package com.assignment.catawiki.pokemon.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.assignment.catawiki.pokemon.api.PokemonSpeciesRepository
import com.assignment.catawiki.pokemon.impl.PokemonSpeciesRepositoryImpl
import com.assignment.catawiki.pokemon.impl.local.PokemonSpeciesFeedPaginationDataSource
import com.assignment.catawiki.pokemon.impl.local.model.PokemonSpeciesFeedPaginationDataSourceImpl
import com.assignment.catawiki.pokemon.impl.remote.PokemonSpeciesRemoteDataSource
import com.assignment.catawiki.pokemon.impl.remote.PokemonSpeciesRemoteDataSourceImpl
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
