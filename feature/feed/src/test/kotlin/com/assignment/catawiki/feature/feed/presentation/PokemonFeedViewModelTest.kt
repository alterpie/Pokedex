package com.assignment.catawiki.feature.feed.presentation

import androidx.lifecycle.SavedStateHandle
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.Event
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.State
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedFeature
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Test

internal class PokemonFeedViewModelTest {

    @Test
    fun `should get feed and initial page on init`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))

        val feature = mockk<PokemonFeedFeature> {
            every { initialState } returns State()
            every { collectState(any()) } returns flowOf()
        }

        PokemonFeedViewModel(SavedStateHandle(), feature)

        coVerify {
            feature.onEvent(Event.GetFeed)
            feature.onEvent(Event.GetInitialPage)
        }
        Dispatchers.resetMain()
    }
}
