package com.yerastov.assignment.pokedex.feature.details.presentation

import androidx.lifecycle.SavedStateHandle
import com.yerastov.assignment.pokedex.feature.details.mvi.PokemonDetailsContract.Event
import com.yerastov.assignment.pokedex.feature.details.mvi.PokemonDetailsContract.State
import com.yerastov.assignment.pokedex.feature.details.mvi.PokemonDetailsFeature
import com.yerastov.assignment.pokedex.feature.details.navigation.POKEMON_ID_NAV_PARAM
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

internal class PokemonDetailsViewModelTest {

    @Test
    fun `should get pokemon species and details on init`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val feature = mockk<PokemonDetailsFeature> {
            every { initialState } returns State()
            every { collectState(any()) } returns flowOf()
        }
        PokemonDetailsViewModel(SavedStateHandle(mapOf(POKEMON_ID_NAV_PARAM to 42L)), feature)

        coVerify {
            feature.onEvent(Event.GetPokemonSpecies(42L))
            feature.onEvent(Event.GetPokemonDetails(42L))
        }
        Dispatchers.resetMain()
    }
}
