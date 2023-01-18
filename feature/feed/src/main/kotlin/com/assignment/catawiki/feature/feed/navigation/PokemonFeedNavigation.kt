package com.assignment.catawiki.feature.feed.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.assignment.catawiki.feature.feed.ui.PokemonFeedScreen

const val PokemonFeedScreenRoute = "feed"

fun NavGraphBuilder.pokemonFeedScreen() {
    composable(PokemonFeedScreenRoute) {

        PokemonFeedScreen()
    }
}
