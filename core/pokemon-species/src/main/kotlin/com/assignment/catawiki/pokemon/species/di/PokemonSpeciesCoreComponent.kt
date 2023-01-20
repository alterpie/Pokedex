package com.assignment.catawiki.pokemon.species.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
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
import dagger.Provides
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Component(modules = [PokemonCoreModule::class, PokemonCoreProvidesModule::class])
@Singleton
interface PokemonSpeciesCoreComponent {

    val pokemonSpeciesRepository: PokemonSpeciesRepository

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance httpClient: HttpClient,
        ): PokemonSpeciesCoreComponent
    }
}

@Module
private interface PokemonCoreModule {

    @Binds
    fun feedPaginationDataSource(impl: PokemonSpeciesFeedPaginationDataSourceImpl): PokemonSpeciesFeedPaginationDataSource

    @Binds
    fun remoteDataSource(impl: PokemonSpeciesRemoteDataSourceImpl): PokemonSpeciesRemoteDataSource

    @Singleton
    @Binds
    fun repository(impl: PokemonSpeciesRepositoryImpl): PokemonSpeciesRepository
}

@Module
private object PokemonCoreProvidesModule {

    @Singleton
    @Provides
    fun pokemonSpeciesCoreDataStore(context: Context): DataStore<Preferences> {
        return context.pokemonSpeciesCoreDataStore
    }
}

private const val POKEMON_SPECIES_CORE_DATA_STORE = "POKEMON_SPECIES_CORE_DATA_STORE"
private val Context.pokemonSpeciesCoreDataStore by preferencesDataStore(
    POKEMON_SPECIES_CORE_DATA_STORE
)
