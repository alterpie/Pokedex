package com.assignment.catawiki.feature.feed.mvi

import app.cash.turbine.test
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.Effect
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.Event
import com.assignment.catawiki.feature.feed.presentation.model.PokemonSpeciesFeedItem
import com.assignment.catawiki.feature.feed.presentation.usecase.GetPokemonFeedUseCase
import com.assignment.catawiki.pokemon.species.domain.PokemonSpeciesRepository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PokemonFeedActorTest {

    @Test
    fun `should display every feed emission`() = runTest {
        val firstEmission = listOf(buildFeedItem())
        val secondEmission = listOf(buildFeedItem(), buildFeedItem().copy(name = "other name"))
        val getPokemonFeedUseCase = mockk<GetPokemonFeedUseCase> {
            every { execute() } returns flowOf(firstEmission, secondEmission)
        }
        val actor = buildActor(getPokemonFeedUseCase = getPokemonFeedUseCase)

        actor.invoke(Event.GetFeed)
            .test {
                awaitItem() shouldBe Effect.DisplayPokemonFeed(listOf(buildFeedItem()))
                awaitItem() shouldBe Effect.DisplayPokemonFeed(
                    listOf(buildFeedItem(), buildFeedItem().copy(name = "other name"))
                )
                awaitComplete()
            }
    }

    @Nested
    inner class TestGetFeedNextPage {
        @Test
        fun `should display pagination loading and get next page with refresh flag set to false`() =
            runTest {
                val repository = mockk<PokemonSpeciesRepository> {
                    coEvery { getNextSpeciesPage(false) } returns Result.success(Unit)
                }
                val actor = buildActor(pokemonSpeciesRepository = repository)

                actor.invoke(Event.GetFeedNextPage)
                    .test {
                        awaitItem() shouldBe Effect.DisplayPaginationLoading
                        awaitComplete()
                    }
                coVerify { repository.getNextSpeciesPage(false) }
            }

        @Test
        fun `should display pagination loading error`() = runTest {
            val repository = mockk<PokemonSpeciesRepository> {
                coEvery { getNextSpeciesPage(false) } returns Result.failure(IllegalStateException())
            }
            val actor = buildActor(pokemonSpeciesRepository = repository)

            actor.invoke(Event.GetFeedNextPage)
                .test {
                    awaitItem() shouldBe Effect.DisplayPaginationLoading
                    awaitItem() shouldBe Effect.DisplayPaginationFailure
                    awaitComplete()
                }
        }
    }

    @Nested
    inner class TestRefreshFeed {

        @Test
        fun `should display refresh and get next page with refresh flag set to true`() = runTest {
            val repository = mockk<PokemonSpeciesRepository> {
                coEvery { getNextSpeciesPage(true) } returns Result.success(Unit)
            }
            val actor = buildActor(pokemonSpeciesRepository = repository)

            actor.invoke(Event.RefreshFeed)
                .test {
                    awaitItem() shouldBe Effect.DisplayRefresh
                    awaitComplete()
                }
            coVerify { repository.getNextSpeciesPage(true) }
        }

        @Test
        fun `should display loading error`() = runTest {
            val repository = mockk<PokemonSpeciesRepository> {
                coEvery { getNextSpeciesPage(true) } returns Result.failure(IllegalStateException())
            }
            val actor = buildActor(pokemonSpeciesRepository = repository)

            actor.invoke(Event.RefreshFeed)
                .test {
                    awaitItem() shouldBe Effect.DisplayRefresh
                    awaitItem() shouldBe Effect.DisplayLoadingFailure
                    awaitComplete()
                }
        }
    }

    @Nested
    inner class TestRetryLoadFeed {

        @Test
        fun `should display refresh and get next page with refresh flag set to true`() = runTest {
            val repository = mockk<PokemonSpeciesRepository> {
                coEvery { getNextSpeciesPage(true) } returns Result.success(Unit)
            }
            val actor = buildActor(pokemonSpeciesRepository = repository)

            actor.invoke(Event.RetryLoadFeed)
                .test {
                    awaitItem() shouldBe Effect.DisplayRefresh
                    awaitComplete()
                }
            coVerify { repository.getNextSpeciesPage(true) }
        }

        @Test
        fun `should display loading error`() = runTest {
            val repository = mockk<PokemonSpeciesRepository> {
                coEvery { getNextSpeciesPage(true) } returns Result.failure(IllegalStateException())
            }
            val actor = buildActor(pokemonSpeciesRepository = repository)

            actor.invoke(Event.RetryLoadFeed)
                .test {
                    awaitItem() shouldBe Effect.DisplayRefresh
                    awaitItem() shouldBe Effect.DisplayLoadingFailure
                    awaitComplete()
                }
        }
    }

    @Nested
    inner class TestGetInitialPage {

        @Test
        fun `should display refresh and get next page with refresh flag set to false when does not have cached data`() =
            runTest {
                val repository = mockk<PokemonSpeciesRepository> {
                    coEvery { getNextSpeciesPage(false) } returns Result.success(Unit)
                    coEvery { getStoredItemsCount() } returns 0L
                }
                val actor = buildActor(pokemonSpeciesRepository = repository)

                actor.invoke(Event.GetInitialPage)
                    .test {
                        awaitItem() shouldBe Effect.DisplayRefresh
                        awaitComplete()
                    }
                coVerifyOrder {
                    repository.getStoredItemsCount()
                    repository.getNextSpeciesPage(false)
                }
            }

        @Test
        fun `should do nothing when has cached data`() = runTest {
            val repository = mockk<PokemonSpeciesRepository> {
                coEvery { getStoredItemsCount() } returns 42L
            }
            val actor = buildActor(pokemonSpeciesRepository = repository)

            actor.invoke(Event.GetInitialPage)
                .test {
                    awaitComplete()
                }
            coVerify { repository.getStoredItemsCount() }
            coVerify(exactly = 0) { repository.getNextSpeciesPage(any()) }
        }

        @Test
        fun `should display loading error`() = runTest {
            val repository = mockk<PokemonSpeciesRepository> {
                coEvery { getStoredItemsCount() } returns 0L
                coEvery { getNextSpeciesPage(false) } returns Result.failure(IllegalStateException())
            }
            val actor = buildActor(pokemonSpeciesRepository = repository)

            actor.invoke(Event.GetInitialPage)
                .test {
                    awaitItem() shouldBe Effect.DisplayRefresh
                    awaitItem() shouldBe Effect.DisplayLoadingFailure
                    awaitComplete()
                }
        }
    }

    private fun buildActor(
        getPokemonFeedUseCase: GetPokemonFeedUseCase = mockk(),
        pokemonSpeciesRepository: PokemonSpeciesRepository = mockk(),
    ): PokemonFeedActor {
        return PokemonFeedActor(getPokemonFeedUseCase, pokemonSpeciesRepository)
    }

    private fun buildFeedItem(): PokemonSpeciesFeedItem {
        return PokemonSpeciesFeedItem(42L, "name", "image")
    }
}
