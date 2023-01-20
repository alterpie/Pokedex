package com.assignment.catawiki.details.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.assignment.catawiki.details.PokemonDetailsViewModel
import com.assignment.catawiki.details.di.PokemonDetailsComponentInjector
import com.assignment.catawiki.details.ui.PokemonDetailsScreen

internal const val POKEMON_ID_NAV_PARAM = "pokemonId"
private const val PokemonDetailsScreenRoute = "details/{$POKEMON_ID_NAV_PARAM}"
fun pokemonDetailsScreenRoute(pokemonId: Long): String {
    return "details/$pokemonId"
}

fun NavGraphBuilder.pokemonDetailsScreen() {
    composable(
        PokemonDetailsScreenRoute,
        arguments = listOf(navArgument(POKEMON_ID_NAV_PARAM) { type = NavType.LongType })
    ) {
        val viewModel = viewModel<PokemonDetailsViewModel>(
            factory = PokemonDetailsComponentInjector.get(LocalContext.current).pokemonDetailsViewModelFactory
        )
        val state by viewModel.state.collectAsState()

        PokemonDetailsScreen(state = state)
    }
}
