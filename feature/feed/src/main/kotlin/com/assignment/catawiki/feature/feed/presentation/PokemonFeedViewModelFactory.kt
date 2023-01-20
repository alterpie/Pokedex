package com.assignment.catawiki.feature.feed.presentation

import androidx.lifecycle.SavedStateHandle
import com.assignment.catawiki.di.viewModel.GenericViewModelFactory
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedFeature
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
