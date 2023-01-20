package com.assignment.catawiki.pokemon.species.impl

import com.assignment.catawiki.pokemon.species.BuildConfig
import com.assignment.catawiki.pokemon.species.impl.PokemonSpeciesRepositoryImpl
import com.assignment.catawiki.pokemon.species.impl.local.PokemonSpeciesFeedPaginationDataSource
import com.assignment.catawiki.pokemon.species.impl.local.model.PaginationData
import com.assignment.catawiki.pokemon.species.impl.remote.PokemonSpeciesRemoteDataSource
import com.assignment.catawiki.pokemon.species.impl.remote.model.PokemonSpeciesFeedPaginationDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class PokemonSpeciesRepositoryImplTest {

    @Test
    fun `should use stored pagination data to fetch next pokemon page`() = runTest {
        val paginationDataDataSource = mockk<PokemonSpeciesFeedPaginationDataSource> {
            coEvery { getPaginationData() } returns PaginationData(42L, "next_url")
        }
        val remoteDataSource = mockk<PokemonSpeciesRemoteDataSource>()

        val repository = PokemonSpeciesRepositoryImpl(
            remoteDataSource,
            paginationDataDataSource,
            mockk(),
            mockk(),
            mockk()
        )

        repository.getNextPokemonPage()

        coVerify {
            remoteDataSource.fetchPokemonPage("next_url")
        }
    }

    @Test
    fun `should use default url to fetch next pokemon page when stored pagination data is not available`() =
        runTest {
            val paginationDataDataSource = mockk<PokemonSpeciesFeedPaginationDataSource> {
                coEvery { getPaginationData() } returns null
            }
            val remoteDataSource = mockk<PokemonSpeciesRemoteDataSource>()

            val repository = PokemonSpeciesRepositoryImpl(
                remoteDataSource,
                paginationDataDataSource,
                mockk(),
                mockk(),
                mockk()
            )

            repository.getNextPokemonPage()

            coVerify {
                remoteDataSource.fetchPokemonPage(BuildConfig.INITIAL_FEED_URL_PATH)
            }
        }

    @Test
    fun `should save pagination data when received feed response`() = runTest {
        val paginationDataDataSource = mockk<PokemonSpeciesFeedPaginationDataSource> {
            coEvery { getPaginationData() } returns null
        }
        val remoteDataSource = mockk<PokemonSpeciesRemoteDataSource> {
            coEvery { fetchPokemonPage(any()) } returns PokemonSpeciesFeedPaginationDto(
                42L,
                "next_url",
                emptyList(),
            )
        }

        val repository = PokemonSpeciesRepositoryImpl(
            remoteDataSource,
            paginationDataDataSource,
            mockk(),
            mockk(),
            mockk()
        )

        repository.getNextPokemonPage()

        coVerify {
            paginationDataDataSource.savePaginationData(PaginationData(42L, "next_url"))
        }
    }
}
