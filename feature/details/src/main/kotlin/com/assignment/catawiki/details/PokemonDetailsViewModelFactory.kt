package com.assignment.catawiki.details

import androidx.lifecycle.SavedStateHandle
import com.assignment.catawiki.details.mvi.PokemonDetailsFeature
import com.assignment.catawiki.di.viewModel.GenericViewModelFactory
import javax.inject.Inject

class PokemonDetailsViewModelFactory @Inject constructor(
    private val feature: PokemonDetailsFeature,
) : GenericViewModelFactory<PokemonDetailsViewModel>() {

    override fun createViewModel(handle: SavedStateHandle): PokemonDetailsViewModel {
        return PokemonDetailsViewModel(handle, feature)
    }
}
