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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.assignment.catawiki.details.mvi.PokemonDetailsContract.State

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
        if (state.evolvesIntoImage != null && state.evolvesIntoName != null) {
            BasicText(text = state.evolvesIntoName)
            Image(
                modifier = Modifier.size(80.dp),
                painter = rememberAsyncImagePainter(model = state.evolvesIntoImage),
                contentDescription = null,
            )
        }
    }
}
