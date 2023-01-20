package com.assignment.catawiki.pokemon.species.data.pagination.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.assignment.catawiki.pokemon.species.data.pagination.PokemonSpeciesFeedPaginationDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class PokemonSpeciesFeedPaginationDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : PokemonSpeciesFeedPaginationDataSource {

    override suspend fun getPaginationData(): PaginationData? {
        return dataStore.data.map { preferences ->
            val count = preferences[KEY_COUNT]
            if (count != null) {
                PaginationData(count, preferences[KEY_NEXT])
            } else {
                null
            }
        }
            .firstOrNull()
    }

    override suspend fun savePaginationData(paginationData: PaginationData) {
        dataStore.edit { preferences ->
            preferences[KEY_COUNT] = paginationData.count
            if (paginationData.next != null) {
                preferences[KEY_NEXT] = paginationData.next
            } else {
                preferences.remove(KEY_NEXT)
            }
        }
    }

    companion object {
        val KEY_COUNT = longPreferencesKey("KEY_COUNT")
        val KEY_NEXT = stringPreferencesKey("KEY_NEXT")
    }
}
