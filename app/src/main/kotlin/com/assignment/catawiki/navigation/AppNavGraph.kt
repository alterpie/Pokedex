package com.assignment.catawiki.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.assignment.catawiki.feature.feed.navigation.PokemonFeedScreenRoute
import com.assignment.catawiki.feature.feed.navigation.pokemonFeedScreen

@Composable
fun AppNavGraph() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PokemonFeedScreenRoute
    ) {
        pokemonFeedScreen(onPokemonClick = { /*TODO navigate to details*/ })
    }
}