package com.assignment.catawiki.feature.feed.ui

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.pullRefreshIndicatorTransform
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.assignment.catawiki.design.button.TextButton
import com.assignment.catawiki.design.gradient.BottomGradient
import com.assignment.catawiki.design.gradient.TopGradient
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.State
import java.util.*
import com.assignment.catawiki.design.R as DesignR

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun PokemonFeedScreen(
    state: State,
    onPokemonClick: (Long) -> Unit,
    onShouldLoadNextPage: () -> Unit,
    onRefreshScreen: () -> Unit,
    onRetryClick: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(state.loadingError) {
        if (state.loadingError is State.LoadingError.PaginationLoadingFailed) {
            Toast.makeText(
                context,
                context.getString(DesignR.string.error_loading_feed_next_page_failed),
                Toast.LENGTH_LONG
            ).show()
        }
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.loadingState is State.LoadingState.Refresh,
        onRefresh = onRefreshScreen,
    )
    val rotation by animateFloatAsState(pullRefreshState.progress * 120)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
            .pullRefresh(state = pullRefreshState)
    ) {
        val lazyListState = rememberLazyListState()
        val shouldPaginate by remember {
            derivedStateOf {
                val lastVisibleItemIndex =
                    lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                val totalItemCount = lazyListState.layoutInfo.totalItemsCount
                state.loadingState == null && lastVisibleItemIndex >= totalItemCount - 5
            }
        }
        LaunchedEffect(shouldPaginate) {
            if (shouldPaginate && state.items.isNotEmpty()) {
                onShouldLoadNextPage()
            }
        }
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                start = 16.dp,
                end = 16.dp,
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateTopPadding()
            )
        ) {
            items(state.items, key = { item -> item.id }) { item ->
                PokemonItem(
                    name = item.name,
                    image = item.imageUrl,
                    onClick = { onPokemonClick(item.id) }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                PaginationLoadingIndicator(state.loadingState is State.LoadingState.Pagination)
            }
        }

        if (state.loadingError is State.LoadingError.LoadingFailed) {
            InitialLoadingFailedSection(onRetryClick = onRetryClick)
        }

        PullRefreshLoadingIndicator(
            pullRefreshState = pullRefreshState,
            rotation = rotation,
            isRefreshing = state.loadingState is State.LoadingState.Refresh,
        )
        TopGradient()
        BottomGradient()
    }
}

@Composable
private fun PokemonItem(
    name: String,
    image: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = MaterialTheme.colors.onBackground,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colors.surface)
            .clickable(onClick = onClick)
            .padding(16.dp)
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.size(70.dp),
            painter = rememberAsyncImagePainter(model = image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        BasicText(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onSurface),
        )
    }
}

@Composable
private fun PaginationLoadingIndicator(isLoading: Boolean) {
    if (isLoading) {
        val infiniteTransition = rememberInfiniteTransition()
        val angle by infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 360F,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing)
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .size(24.dp)
                    .rotate(angle),
                painter = painterResource(DesignR.drawable.ic_pokeball),
                contentDescription = null,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BoxScope.PullRefreshLoadingIndicator(
    pullRefreshState: PullRefreshState,
    rotation: Float,
    isRefreshing: Boolean,
) {
    Crossfade(
        modifier = Modifier
            .align(Alignment.TopCenter),
        targetState = isRefreshing,
    ) { refreshing ->
        if (refreshing) {
            val infiniteTransition = rememberInfiniteTransition()
            val angle by infiniteTransition.animateFloat(
                initialValue = 0F,
                targetValue = 360F,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing)
                )
            )
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .pullRefreshIndicatorTransform(pullRefreshState)
                    .rotate(angle),
                painter = painterResource(DesignR.drawable.ic_pokeball),
                contentDescription = null,
            )
        } else {
            Image(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(40.dp)
                    .pullRefreshIndicatorTransform(pullRefreshState)
                    .rotate(rotation),
                painter = painterResource(DesignR.drawable.ic_pokeball),
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun BoxScope.InitialLoadingFailedSection(onRetryClick: () -> Unit) {
    Column(
        modifier = Modifier.align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicText(
            text = stringResource(DesignR.string.error_loading_feed_failed),
            style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.onBackground),
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(text = stringResource(DesignR.string.retry), onClick = onRetryClick)
    }
}

@Preview
@Composable
private fun PokemonItemPreview() {
    Column {
        PokemonItem(name = "Psyduck", image = "", onClick = {})
        PokemonItem(name = "Pikacu", image = "", onClick = {})
        PokemonItem(name = "Snorlax", image = "", onClick = {})
    }
}
