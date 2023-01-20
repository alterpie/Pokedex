package com.assignment.catawiki.pokemon.species.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.assignment.catawiki.pokemon.species.domain.PokemonSpeciesRepository
import com.assignment.catawiki.pokemon.species.data.species.PokemonSpeciesRepositoryImpl
import com.assignment.catawiki.pokemon.species.data.species.local.PokemonSpeciesLocalDataSourceImpl
import com.assignment.catawiki.pokemon.species.data.species.local.SpeciesDatabase
import com.assignment.catawiki.pokemon.species.data.pagination.PokemonSpeciesFeedPaginationDataSource
import com.assignment.catawiki.pokemon.species.data.pagination.model.PokemonSpeciesFeedPaginationDataSourceImpl
import com.assignment.catawiki.pokemon.species.data.species.local.species.PokemonSpeciesLocalDataSource
import com.assignment.catawiki.pokemon.species.data.species.local.species.SpeciesDao
import com.assignment.catawiki.pokemon.species.data.species.remote.PokemonSpeciesRemoteDataSource
import com.assignment.catawiki.pokemon.species.data.species.remote.PokemonSpeciesRemoteDataSourceImpl
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

    @Binds
    fun localDataSource(impl: PokemonSpeciesLocalDataSourceImpl): PokemonSpeciesLocalDataSource

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

    @Provides
    @Singleton
    fun speciesDatabase(context: Context): SpeciesDatabase {
        return Room.databaseBuilder(
            context,
            SpeciesDatabase::class.java,
            "species-database"
        ).build()
    }

    @Provides
    fun speciesDao(speciesDatabase: SpeciesDatabase): SpeciesDao {
        return speciesDatabase.speciesDao()
    }
}

private const val POKEMON_SPECIES_CORE_DATA_STORE = "POKEMON_SPECIES_CORE_DATA_STORE"
private val Context.pokemonSpeciesCoreDataStore by preferencesDataStore(
    POKEMON_SPECIES_CORE_DATA_STORE
)
