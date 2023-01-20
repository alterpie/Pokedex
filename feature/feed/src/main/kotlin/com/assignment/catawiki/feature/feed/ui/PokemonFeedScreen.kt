package com.assignment.catawiki.feature.feed.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.assignment.catawiki.feature.feed.mvi.PokemonFeedContract.State
import java.util.*

@Composable
internal fun PokemonFeedScreen(
    state: State,
    onPokemonClick: (Long) -> Unit,
    onShouldLoadNextPage: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(state.loadingError) {
        if (state.loadingError != null) {
            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        val lazyListState = rememberLazyListState()
        val shouldPaginate by remember {
            derivedStateOf {
                val lastVisibleItemIndex =
                    lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                val totalItemCount = lazyListState.layoutInfo.totalItemsCount
                lastVisibleItemIndex >= totalItemCount - 5
            }
        }
        LaunchedEffect(shouldPaginate) {
            if (shouldPaginate && state.items.isNotEmpty()) {
                onShouldLoadNextPage()
            }
        }
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(state.items, key = { _, item -> item.id }) { index, item ->
                PokemonItem(
                    name = item.name,
                    image = item.imageUrl,
                    onClick = { onPokemonClick(item.id) }
                )

                if (index != state.items.lastIndex) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

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
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.size(60.dp),
            painter = rememberAsyncImagePainter(model = image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        BasicText(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
        )
    }
}

@Composable
private fun BoxScope.TopGradient() {
    Box(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
            .height(40.dp)
    )
}

@Composable
private fun BoxScope.BottomGradient() {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black
                    )
                )
            )
            .height(40.dp)
    )
}

@Preview
@Composable
private fun PokemonItemPreview() {
    Column {
        PokemonItem(name = "Psyduck", image = "", onClick = {})
    }
}
