package com.yerastov.assignment.pokedex.core.pokemon.species.data.pagination

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.yerastov.assignment.pokedex.core.pokemon.species.data.pagination.model.PaginationData
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class PokemonSpeciesFeedPaginationDataSourceImplTest {

    @Test
    fun `should get stored pagination data`() = runTest {
        val preferences = mockk<Preferences> {
            every { get(PokemonSpeciesFeedPaginationDataSourceImpl.KEY_COUNT) } returns 42
            every { get(PokemonSpeciesFeedPaginationDataSourceImpl.KEY_NEXT) } returns "next"
        }
        val dataStore = mockk<DataStore<Preferences>> {
            every { data } returns flowOf(preferences)
        }

        val dataSource = PokemonSpeciesFeedPaginationDataSourceImpl(dataStore)

        val paginationData = dataSource.getPaginationData()

        paginationData shouldBe PaginationData(42, "next")
    }
}
