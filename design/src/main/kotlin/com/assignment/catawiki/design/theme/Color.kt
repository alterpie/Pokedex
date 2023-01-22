package com.assignment.catawiki.design.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color


internal val DarkColorPalette = darkColors(
    background = Color(0xFF383333),
    surface = Color(0xFFEEEEEE),
    onBackground = Color(0xFFE7E7E7),
    onSurface = Color(0xFF252525),
    secondary = Color(0xFF305800),
    error = Color(0xFF860101),
)

internal val LightColorPalette = lightColors(
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFDAD0D0),
    onBackground = Color(0xFF161616),
    onSurface = Color(0xFF292929),
    secondary = Color(0xFF4F9102),
    error = Color(0xFFBE0000),
)

val Colors.topGradient: List<Color>
    get() = if (isLight) {
        listOf(
            Color.Black,
            Color.Transparent
        )
    } else {
        listOf(
            Color.White,
            Color.Transparent
        )
    }

val Colors.bottomGradient: List<Color>
    get() = if (isLight) {
        listOf(
            Color.Transparent,
            Color.Black,
        )
    } else {
        listOf(
            Color.Transparent,
            Color.White,
        )
    }
