package com.yerastov.assignment.pokedex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.yerastov.assignment.pokedex.feature.details.navigation.pokemonDetailsScreen
import com.yerastov.assignment.pokedex.feature.details.navigation.pokemonDetailsScreenRoute
import com.yerastov.assignment.pokedex.feature.feed.navigation.PokemonFeedScreenRoute
import com.yerastov.assignment.pokedex.feature.feed.navigation.pokemonFeedScreen

@Composable
fun AppNavGraph() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PokemonFeedScreenRoute,
    ) {
        pokemonFeedScreen(onPokemonClick = { pokemonId ->
            navController.navigate(pokemonDetailsScreenRoute(pokemonId))
        })
        pokemonDetailsScreen(onBackClick = { navController.navigateUp() })
    }
}
