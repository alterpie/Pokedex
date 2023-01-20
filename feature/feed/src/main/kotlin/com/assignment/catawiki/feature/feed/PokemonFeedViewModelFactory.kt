package com.assignment.catawiki.feature.feed

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.assignment.catawiki.di.viewModel.GenericViewModelFactory
import com.assignment.catawiki.pokemon.species.di.PokemonSpeciesCoreComponentInjector

internal class PokemonFeedViewModelFactory(
    private val context: Context,
) : GenericViewModelFactory<PokemonFeedViewModel>() {
    override fun createViewModel(handle: SavedStateHandle): PokemonFeedViewModel {
        return PokemonFeedViewModel(
            handle,
            PokemonSpeciesCoreComponentInjector.get(context.applicationContext).pokemonSpeciesRepository,
        )
    }
}
