package com.yerastov.assignment.pokedex.feature.feed.presentation

import androidx.lifecycle.SavedStateHandle
import com.yerastov.assignment.pokedex.core.di.viewModel.GenericViewModelFactory
import com.yerastov.assignment.pokedex.feature.feed.mvi.PokemonFeedFeature
import javax.inject.Inject

class PokemonFeedViewModelFactory @Inject constructor(
    private val feature: PokemonFeedFeature,
) : GenericViewModelFactory<PokemonFeedViewModel>() {
    override fun createViewModel(handle: SavedStateHandle): PokemonFeedViewModel {
        return PokemonFeedViewModel(
            handle,
            feature,
        )
    }
}
