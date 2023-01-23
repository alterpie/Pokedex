package com.assignment.catawiki.pokemon.species.data.species

import app.cash.turbine.testIn
import com.assignment.catawiki.pokemon.species.BuildConfig
import com.assignment.catawiki.pokemon.species.data.pagination.PokemonSpeciesFeedPaginationDataSource
import com.assignment.catawiki.pokemon.species.data.pagination.model.PaginationData
import com.assignment.catawiki.pokemon.species.data.species.local.PokemonSpeciesLocalDataSource
import com.assignment.catawiki.pokemon.species.data.species.local.model.SpeciesEntity
import com.assignment.catawiki.pokemon.species.data.species.local.model.UpdateCaptureRateDifference
import com.assignment.catawiki.pokemon.species.data.species.local.model.UpdateSpeciesDetails
import com.assignment.catawiki.pokemon.species.data.species.local.model.UpdateSpeciesEvolution
import com.assignment.catawiki.pokemon.species.data.species.mapper.EvolutionChainDtoMapper
import com.assignment.catawiki.pokemon.species.data.species.mapper.PokemonSpeciesDetailsDtoMapper
import com.assignment.catawiki.pokemon.species.data.species.mapper.PokemonSpeciesDtoMapper
import com.assignment.catawiki.pokemon.species.data.species.mapper.SpeciesEntityMapper
import com.assignment.catawiki.pokemon.species.data.species.remote.PokemonSpeciesRemoteDataSource
import com.assignment.catawiki.pokemon.species.data.species.remote.model.PokemonSpeciesDetailsDto
import com.assignment.catawiki.pokemon.species.data.species.remote.model.PokemonSpeciesFeedPaginationDto
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PokemonSpeciesRepositoryImplTest {

    @Nested
    inner class TestGetNextPage {
        @Test
        fun `should use stored pagination data to fetch next pokemon page`() = runTest {
            val paginationDataDataSource = mockk<PokemonSpeciesFeedPaginationDataSource> {
                coEvery { getPaginationData() } returns PaginationData(42L, "next_url")
            }
            val remoteDataSource = mockk<PokemonSpeciesRemoteDataSource>()
            val localDataSource = mockk<PokemonSpeciesLocalDataSource>()

            val repository = PokemonSpeciesRepositoryImpl(
                remoteDataSource,
                localDataSource,
                paginationDataDataSource,
                mockk(),
                mockk(),
                mockk(),
                mockk(),
            )

            repository.getNextSpeciesPage(false)

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
                val localDataSource = mockk<PokemonSpeciesLocalDataSource>()

                val repository = PokemonSpeciesRepositoryImpl(
                    remoteDataSource,
                    localDataSource,
                    paginationDataDataSource,
                    mockk(),
                    mockk(),
                    mockk(),
                    mockk(),
                )

                repository.getNextSpeciesPage(false)

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
            val localDataSource = mockk<PokemonSpeciesLocalDataSource>()

            val repository = PokemonSpeciesRepositoryImpl(
                remoteDataSource,
                localDataSource,
                paginationDataDataSource,
                mockk(),
                mockk(),
                mockk(),
                mockk(),
            )

            repository.getNextSpeciesPage(false)

            coVerify {
                paginationDataDataSource.savePaginationData(PaginationData(42L, "next_url"))
            }
        }

        @Test
        fun `should clear pagination data and local storage when refresh flag is true`() = runTest {
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
            val localDataSource = mockk<PokemonSpeciesLocalDataSource>()
            val pokemonSpeciesDtoMapper = mockk<PokemonSpeciesDtoMapper> {
                every { map(any()) } returns mockk()
            }

            val repository = PokemonSpeciesRepositoryImpl(
                remoteDataSource,
                localDataSource,
                paginationDataDataSource,
                pokemonSpeciesDtoMapper,
                mockk(),
                mockk(),
                mockk(),
            )

            repository.getNextSpeciesPage(true)

            coVerify {
                paginationDataDataSource.clearPaginationData()
                localDataSource.removeAll()
            }
        }
    }

    @Test
    fun `should emit updates for species by id`() = runTest {
        val speciesEmitter = MutableSharedFlow<SpeciesEntity>()
        val localDataSource = mockk<PokemonSpeciesLocalDataSource> {
            every { getSpecies(any()) } returns speciesEmitter
        }
        val speciesEntityMapper = mockk<SpeciesEntityMapper> {
            every { map(any()) } returns mockk()
        }

        val repository = PokemonSpeciesRepositoryImpl(
            mockk(),
            localDataSource,
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            speciesEntityMapper,
        )

        val turbine = repository.getPokemonSpecies(42L).testIn(this)

        val initialEntity = buildSpeciesEntity()
        speciesEmitter.emit(initialEntity)
        val updatedEntity = initialEntity.copy(name = "updated")
        speciesEmitter.emit(updatedEntity)

        turbine.cancelAndConsumeRemainingEvents().size shouldBe 2

        verifyOrder {
            speciesEntityMapper.map(initialEntity)
            speciesEntityMapper.map(updatedEntity)
        }
    }

    @Test
    fun `should emit updates for list of species`() = runTest {
        val speciesEmitter = MutableSharedFlow<List<SpeciesEntity>>()
        val localDataSource = mockk<PokemonSpeciesLocalDataSource> {
            every { getAllSpecies() } returns speciesEmitter
        }
        val speciesEntityMapper = mockk<SpeciesEntityMapper> {
            every { map(any()) } returns mockk()
        }

        val repository = PokemonSpeciesRepositoryImpl(
            mockk(),
            localDataSource,
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            speciesEntityMapper,
        )

        val turbine = repository.getAllPokemonSpecies().testIn(this)

        val initialEntities = listOf(buildSpeciesEntity().copy(id = 1L))
        speciesEmitter.emit(initialEntities)
        val updatedEntities = listOf(
            buildSpeciesEntity().copy(id = 1L),
            buildSpeciesEntity().copy(id = 2L),
        )
        speciesEmitter.emit(updatedEntities)

        turbine.cancelAndConsumeRemainingEvents().size shouldBe 2

        verifyOrder {
            initialEntities.forEach { speciesEntityMapper.map(it) }
            updatedEntities.forEach { speciesEntityMapper.map(it) }
        }
    }

    @Nested
    inner class TestGetSpeciesEvolution {
        @Test
        fun `should get from remote data with evolution details source when chain url is present`() =
            runTest {
                val remoteDataSource = mockk<PokemonSpeciesRemoteDataSource> {
                    coEvery { fetchEvolutionChain(any()) } returns mockk()
                    coEvery { fetchPokemonDetails(any()) } returns PokemonSpeciesDetailsDto(
                        43L,
                        "evolved",
                        emptyList(),
                        null,
                        10
                    )
                }
                val localDataSource = mockk<PokemonSpeciesLocalDataSource> {
                    every { getSpecies(any()) } returns flowOf(
                        buildSpeciesEntity().copy(
                            evolutionChainUrl = "url",
                            captureRate = 5,
                        )
                    )
                }
                val evolutionChainDtoMapper = mockk<EvolutionChainDtoMapper> {
                    every { map(any(), any()) } returns SpeciesEntity.Evolution.EvolvesTo(
                        43L,
                        "evolved",
                        "image"
                    )
                }
                val pokemonSpeciesDetailsDtoMapper = mockk<PokemonSpeciesDetailsDtoMapper> {
                    every { map(any()) } returns UpdateSpeciesDetails(43L, "desc", 10, "url")
                }

                val repository = PokemonSpeciesRepositoryImpl(
                    remoteDataSource,
                    localDataSource,
                    mockk(),
                    mockk(),
                    pokemonSpeciesDetailsDtoMapper,
                    evolutionChainDtoMapper,
                    mockk(),
                )

                val result = repository.getSpeciesEvolution(42L)

                result.isSuccess shouldBe true
                coVerifyOrder {
                    localDataSource.getSpecies(42L)
                    remoteDataSource.fetchEvolutionChain("url")
                    evolutionChainDtoMapper.map(any(), "name")
                    remoteDataSource.fetchPokemonDetails(43L)
                    localDataSource.updateDetails(UpdateSpeciesDetails(43L, "desc", 10, "url"))
                    localDataSource.updateCaptureRateDifference(
                        UpdateCaptureRateDifference(
                            42L,
                            -5
                        )
                    )
                    localDataSource.updateEvolution(
                        UpdateSpeciesEvolution(
                            42L, SpeciesEntity.Evolution.EvolvesTo(
                                43L,
                                "evolved",
                                "image"
                            )
                        )
                    )
                }
            }

        @Test
        fun `should update to final evolution when chain url is absent`() = runTest {
            val remoteDataSource = mockk<PokemonSpeciesRemoteDataSource>()
            val localDataSource = mockk<PokemonSpeciesLocalDataSource> {
                every { getSpecies(any()) } returns flowOf(
                    buildSpeciesEntity().copy(evolutionChainUrl = null)
                )
            }
            val evolutionChainDtoMapper = mockk<EvolutionChainDtoMapper>()
            val pokemonSpeciesDetailsDtoMapper = mockk<PokemonSpeciesDetailsDtoMapper>()

            val repository = PokemonSpeciesRepositoryImpl(
                remoteDataSource,
                localDataSource,
                mockk(),
                mockk(),
                pokemonSpeciesDetailsDtoMapper,
                evolutionChainDtoMapper,
                mockk(),
            )

            val result = repository.getSpeciesEvolution(42L)

            result.isSuccess shouldBe true
            coVerifyOrder {
                localDataSource.getSpecies(42L)
                localDataSource.updateEvolution(
                    UpdateSpeciesEvolution(42L, SpeciesEntity.Evolution.Final)
                )
            }
        }
    }

    @Nested
    inner class TestGetSpeciesDetails {
        @Test
        fun `should not get from remote data source when has cached data`() = runTest {
            val remoteDataSource = mockk<PokemonSpeciesRemoteDataSource> {
                coEvery { fetchEvolutionChain(any()) } returns mockk()
            }
            val localDataSource = mockk<PokemonSpeciesLocalDataSource> {
                every { getSpecies(any()) } returns flowOf(
                    buildSpeciesEntity().copy(
                        evolution = SpeciesEntity.Evolution.Final,
                        description = "description"
                    )
                )
            }

            val repository = PokemonSpeciesRepositoryImpl(
                remoteDataSource,
                localDataSource,
                mockk(),
                mockk(),
                mockk(),
                mockk(),
                mockk(),
            )

            val result = repository.getSpeciesDetails(42L)

            result.isSuccess shouldBe true
            coVerify { localDataSource.getSpecies(42L) }
            coVerify(exactly = 0) { remoteDataSource.fetchPokemonDetails(any()) }
        }

        @Test
        fun `should get from remote data source when there are no cached data`() = runTest {
            val remoteDataSource = mockk<PokemonSpeciesRemoteDataSource> {
                coEvery { fetchEvolutionChain(any()) } returns mockk()
                coEvery { fetchPokemonDetails(any()) } returns PokemonSpeciesDetailsDto(
                    42L, "name", emptyList(), null, 10
                )
            }
            val localDataSource = mockk<PokemonSpeciesLocalDataSource> {
                every { getSpecies(any()) } returns flowOf(
                    buildSpeciesEntity().copy(
                        description = null,
                        evolution = null,
                    )
                )
            }
            val pokemonSpeciesDetailsDtoMapper = mockk<PokemonSpeciesDetailsDtoMapper> {
                every { map(any()) } returns UpdateSpeciesDetails(
                    42L,
                    "description",
                    42,
                    "chain url"
                )
            }
            val evolutionChainDtoMapper = mockk<EvolutionChainDtoMapper> {
                every {
                    map(any(), any())
                } returns SpeciesEntity.Evolution.EvolvesTo(43L, "evolved", "image")
            }

            val repository = PokemonSpeciesRepositoryImpl(
                remoteDataSource,
                localDataSource,
                mockk(),
                mockk(),
                pokemonSpeciesDetailsDtoMapper,
                evolutionChainDtoMapper,
                mockk(),
            )

            val result = repository.getSpeciesDetails(42L)

            result.isSuccess shouldBe true
            coVerifyOrder {
                localDataSource.getSpecies(42L)
                remoteDataSource.fetchPokemonDetails(42L)
                pokemonSpeciesDetailsDtoMapper.map(any())
                localDataSource.updateDetails(any())
                localDataSource.getSpecies(42L)
                remoteDataSource.fetchEvolutionChain("chain url")
                evolutionChainDtoMapper.map(any(), "name")
                remoteDataSource.fetchPokemonDetails(43L)
                localDataSource.updateDetails(any())
                localDataSource.updateCaptureRateDifference(UpdateCaptureRateDifference(42L, 0))
                localDataSource.updateEvolution(
                    UpdateSpeciesEvolution(
                        42L,
                        SpeciesEntity.Evolution.EvolvesTo(43L, "evolved", "image")
                    )
                )
            }
        }

        @Test
        fun `should get species evolution from remote data source details are present but evolution is not`() =
            runTest {
                val remoteDataSource = mockk<PokemonSpeciesRemoteDataSource> {
                    coEvery { fetchEvolutionChain(any()) } returns mockk()
                    coEvery { fetchPokemonDetails(any()) } returns mockk()
                }
                val localDataSource = mockk<PokemonSpeciesLocalDataSource> {
                    every { getSpecies(any()) } returns flowOf(
                        buildSpeciesEntity().copy(
                            description = "description",
                            evolution = null
                        )
                    )
                }
                val pokemonSpeciesDetailsDtoMapper = mockk<PokemonSpeciesDetailsDtoMapper> {
                    every { map(any()) } returns UpdateSpeciesDetails(
                        42L,
                        "description",
                        42,
                        "chain url"
                    )
                }
                val evolutionChainDtoMapper = mockk<EvolutionChainDtoMapper> {
                    every { map(any(), any()) } returns SpeciesEntity.Evolution.Final
                }

                val repository = PokemonSpeciesRepositoryImpl(
                    remoteDataSource,
                    localDataSource,
                    mockk(),
                    mockk(),
                    pokemonSpeciesDetailsDtoMapper,
                    evolutionChainDtoMapper,
                    mockk(),
                )

                val result = repository.getSpeciesDetails(42L)

                result.isSuccess shouldBe true
                coVerifyOrder {
                    localDataSource.getSpecies(42L)
                    remoteDataSource.fetchEvolutionChain("chain url")
                    evolutionChainDtoMapper.map(any(), "name")
                    localDataSource.updateEvolution(
                        UpdateSpeciesEvolution(42L, SpeciesEntity.Evolution.Final)
                    )
                }
            }

        @Test
        fun `should set species evolution to final when evolution url is absent`() = runTest {
            val remoteDataSource = mockk<PokemonSpeciesRemoteDataSource> {
                coEvery { fetchEvolutionChain(any()) } returns mockk()
                coEvery { fetchPokemonDetails(any()) } returns mockk()
            }
            val localDataSource = mockk<PokemonSpeciesLocalDataSource> {
                every { getSpecies(any()) } returns flowOf(
                    buildSpeciesEntity().copy(
                        description = "description",
                        evolution = null,
                        evolutionChainUrl = null,
                    )
                )
            }

            val repository = PokemonSpeciesRepositoryImpl(
                remoteDataSource,
                localDataSource,
                mockk(),
                mockk(),
                mockk(),
                mockk(),
                mockk(),
            )

            val result = repository.getSpeciesDetails(42L)

            result.isSuccess shouldBe true
            coVerifyOrder {
                localDataSource.getSpecies(42L)
                localDataSource.updateEvolution(
                    UpdateSpeciesEvolution(42L, SpeciesEntity.Evolution.Final)
                )
            }
            coVerify(exactly = 0) { remoteDataSource.fetchEvolutionChain(any()) }
        }
    }

    private fun buildSpeciesEntity(): SpeciesEntity {
        return SpeciesEntity(42L, "name", "imageUrl", "description", 42, "chain url", null, 24)
    }
}
