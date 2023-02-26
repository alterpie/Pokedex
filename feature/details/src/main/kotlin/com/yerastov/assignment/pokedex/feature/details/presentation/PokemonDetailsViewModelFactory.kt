package com.yerastov.assignment.pokedex.feature.details.presentation

import androidx.lifecycle.SavedStateHandle
import com.yerastov.assignment.pokedex.core.di.viewModel.GenericViewModelFactory
import com.yerastov.assignment.pokedex.feature.details.mvi.PokemonDetailsFeature
import javax.inject.Inject

class PokemonDetailsViewModelFactory @Inject constructor(
    private val feature: PokemonDetailsFeature,
) : GenericViewModelFactory<PokemonDetailsViewModel>() {

    override fun createViewModel(handle: SavedStateHandle): PokemonDetailsViewModel {
        return PokemonDetailsViewModel(handle, feature)
    }
}
