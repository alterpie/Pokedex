package com.assignment.catawiki.details.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.assignment.catawiki.design.button.TextButton
import com.assignment.catawiki.design.gradient.BottomGradient
import com.assignment.catawiki.design.gradient.TopGradient
import com.assignment.catawiki.details.mvi.PokemonDetailsContract.State
import com.assignment.catawiki.pokemon.species.domain.model.PokemonSpecies
import java.util.Locale
import com.assignment.catawiki.design.R as DesignR

@Composable
internal fun PokemonDetailsScreen(
    state: State,
    onRetryLoadDetailsClick: () -> Unit,
    onRetryLoadEvolutionClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(end = 16.dp)
                .size(150.dp)
                .alpha(0.5f),
            painter = rememberAsyncImagePainter(model = state.imageUrl),
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            TopAppBar(onBackClick = onBackClick)
            Spacer(modifier = Modifier.height(8.dp))
            BasicText(
                text = state.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                style = MaterialTheme.typography.h1.copy(color = MaterialTheme.colors.onBackground),
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (state.error is State.Error.LoadingDetailsFailed) {
                ErrorSection(
                    text = stringResource(DesignR.string.error_loading_details),
                    onRetryClick = onRetryLoadDetailsClick
                )
            } else {
                Crossfade(targetState = state.loadingDetails) { loading ->
                    if (loading) {
                        LoadingDetailsPlaceholder()
                    } else {
                        DetailsSection(
                            description = state.description,
                            captureRate = state.captureRate
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            if (state.error == null) {
                Crossfade(targetState = state.loadingEvolution) { loading ->
                    if (loading) {
                        LoadingEvolutionPlaceholder()
                    } else {
                        EvolutionSection(evolution = state.evolution)
                    }
                }
            } else if (state.error is State.Error.LoadingEvolutionFailed) {
                ErrorSection(
                    text = stringResource(DesignR.string.error_loading_evolution),
                    onRetryClick = onRetryLoadEvolutionClick
                )
            }
        }

        TopGradient()
        BottomGradient()
    }
}

@Composable
private fun TopAppBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(DesignR.drawable.ic_arrow_back), contentDescription = null,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onBackground),
            modifier = Modifier.clickable(
                MutableInteractionSource(),
                rememberRipple(bounded = false),
                onClick = onBackClick
            )
        )
    }
}

@Composable
private fun DetailsSection(description: String, captureRate: Int?) {
    Column {
        BasicText(
            text = description,
            style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.onBackground)
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (captureRate != null) {
            val captureRateColor = if (captureRate < 0) {
                MaterialTheme.colors.error
            } else {
                MaterialTheme.colors.secondary
            }

            val captureRateLabel = buildAnnotatedString {
                val captureRateValue = kotlin.math.abs(captureRate)
                val text = stringResource(
                    DesignR.string.template_capture_rate,
                    captureRateValue
                )
                append(text)
                val startIndex = text.indexOf(captureRateValue.toString())
                val endIndex = startIndex + captureRateValue.toString().length
                addStyle(
                    SpanStyle(color = captureRateColor),
                    startIndex,
                    endIndex
                )
            }
            BasicText(
                text = captureRateLabel,
                style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.onBackground)
            )
        }
    }

}

@Composable
private fun EvolutionSection(evolution: PokemonSpecies.Evolution?) {
    Column {
        when (evolution) {
            PokemonSpecies.Evolution.Final -> {
                BasicText(
                    text = stringResource(DesignR.string.final_evolution_link),
                    style = MaterialTheme.typography.h1.copy(color = MaterialTheme.colors.onBackground)
                )
            }
            is PokemonSpecies.Evolution.EvolvesTo -> {
                BasicText(
                    text = stringResource(DesignR.string.evolves_to),
                    style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.onBackground)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colors.onBackground,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(color = MaterialTheme.colors.surface),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier.size(150.dp),
                        painter = rememberAsyncImagePainter(model = evolution.imageUrl),
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicText(
                        modifier = Modifier
                            .padding(bottom = 16.dp, end = 16.dp),
                        text = evolution.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                        },
                        style = MaterialTheme.typography.h1.copy(color = MaterialTheme.colors.onSurface)
                    )
                }
            }
            null -> Unit
        }
    }
}

@Composable
private fun LoadingDetailsPlaceholder() {
    val transition = rememberInfiniteTransition()
    val color by transition.animateColor(
        initialValue = MaterialTheme.colors.surface,
        targetValue = MaterialTheme.colors.surface.copy(alpha = 0.5f),
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        )
    )
    Column {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = color)
                .size(width = 150.dp, height = 28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = color)
                .size(width = 100.dp, height = 28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = color)
                .size(width = 180.dp, height = 28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = color)
                .size(width = 110.dp, height = 28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = color)
                .size(width = 110.dp, height = 28.dp)
        )
    }
}

@Composable
private fun LoadingEvolutionPlaceholder() {
    val bgColorTransition = rememberInfiniteTransition()
    val bgColor by bgColorTransition.animateColor(
        initialValue = MaterialTheme.colors.surface,
        targetValue = MaterialTheme.colors.surface.copy(alpha = 0.5f),
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        )
    )

    val contentColorTransition = rememberInfiniteTransition()
    val contentColor by contentColorTransition.animateColor(
        initialValue = MaterialTheme.colors.onSurface,
        targetValue = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        )
    )
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = bgColor)
                .size(width = 80.dp, height = 28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = bgColor)
                .fillMaxWidth()
                .height(160.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = contentColor)
                    .size(width = 110.dp, height = 28.dp)
            )
        }
    }
}

@Composable
private fun ErrorSection(text: String, onRetryClick: () -> Unit) {
    Column {
        BasicText(
            text = text,
            style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.onBackground)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(text = stringResource(DesignR.string.retry), onClick = onRetryClick)
    }
}
