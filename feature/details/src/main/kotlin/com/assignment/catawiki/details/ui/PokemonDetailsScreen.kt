package com.assignment.catawiki.details.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.assignment.catawiki.details.mvi.PokemonDetailsContract.State
import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpecies

@Composable
internal fun PokemonDetailsScreen(state: State) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        BasicText(text = state.name)
        BasicText(text = state.description)
        if (state.captureRate != null) {
            val captureRateColor = if (state.captureRate < 0) Color.Red else Color.Green
            BasicText(
                text = state.captureRate.toString(),
                style = TextStyle.Default.copy(color = captureRateColor)
            )
        }
        when (state.evolution) {
            PokemonSpecies.Evolution.Final -> {
                BasicText(text = stringResource(com.assignment.catawiki.design.R.string.final_evolution_chain))
            }
            is PokemonSpecies.Evolution.EvolvesTo -> {
                BasicText(text = state.evolution.name)
                Image(
                    modifier = Modifier.size(80.dp),
                    painter = rememberAsyncImagePainter(model = state.evolution.imageUrl),
                    contentDescription = null,
                )
            }
            null -> Unit
        }
    }
}
