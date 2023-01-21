package com.assignment.catawiki.feature.feed.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.assignment.catawiki.feature.feed.presentation.PokemonFeedViewModel
import com.assignment.catawiki.feature.feed.di.PokemonFeedComponentInjector
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.Event
import com.assignment.catawiki.feature.feed.ui.PokemonFeedScreen

const val PokemonFeedScreenRoute = "feed"

fun NavGraphBuilder.pokemonFeedScreen(onPokemonClick: (Long) -> Unit) {
    composable(PokemonFeedScreenRoute) {
        val viewModel = viewModel<PokemonFeedViewModel>(
            factory = PokemonFeedComponentInjector.get(LocalContext.current).pokemonFeedViewModelFactory
        )
        val state by viewModel.state.collectAsState()

        PokemonFeedScreen(
            state = state,
            onPokemonClick = onPokemonClick,
            onShouldLoadNextPage = { viewModel.onEvent(Event.GetFeedNextPage) },
            onRefreshScreen = { viewModel.onEvent(Event.RefreshPage) },
            onRetryClick = { viewModel.onEvent(Event.RetryLoadFeed) }
        )
    }
}
