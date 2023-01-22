package com.assignment.catawiki.design.gradient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.assignment.catawiki.design.theme.bottomGradient
import com.assignment.catawiki.design.theme.topGradient

@Composable
fun BoxScope.TopGradient() {
    Box(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = MaterialTheme.colors.topGradient
                )
            )
            .height(40.dp)
    )
}

@Composable
fun BoxScope.BottomGradient() {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = MaterialTheme.colors.bottomGradient
                )
            )
            .height(40.dp)
    )
}
