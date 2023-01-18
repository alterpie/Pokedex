package com.assignment.catawiki.details.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.assignment.catawiki.details.ui.PokemonDetailsScreen

const val PokemonDetailsScreenRoute = "details"

fun NavGraphBuilder.pokemonDetailsScreen() {

    composable(PokemonDetailsScreenRoute) {
        PokemonDetailsScreen()
    }
}
