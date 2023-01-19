package com.assignment.catawiki.feature.feed.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.assignment.catawiki.feature.feed.PokemonFeedViewModel
import com.assignment.catawiki.feature.feed.PokemonFeedViewModelFactory
import com.assignment.catawiki.feature.feed.ui.PokemonFeedScreen

const val PokemonFeedScreenRoute = "feed"

fun NavGraphBuilder.pokemonFeedScreen(onPokemonClick: (Long) -> Unit) {
    composable(PokemonFeedScreenRoute) {
        val viewModel = viewModel<PokemonFeedViewModel>(factory = PokemonFeedViewModelFactory())
        val state by viewModel.state.collectAsState()

        PokemonFeedScreen(
            state = state,
            onPokemonClick = onPokemonClick
        )
    }
}
