package com.oguzhanaslann.commonui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = spanishGreen,
    primaryVariant = spanishGreen,
    secondary = bronze,
    secondaryVariant = bronze,
    background = arsenic,
    surface = antiFlashWhite,
    error = bronze,
    onPrimary = white,
    onSecondary =  white,
    onBackground = white,
    onSurface = xanadu,
    onError = white
)

private val LightColorPalette = lightColors(
    primary = spanishGreen,
    primaryVariant = spanishGreen,
    secondary = bronze,
    secondaryVariant = bronze,
    background = lotion,
    surface = antiFlashWhite,
    error = bronze,
    onPrimary = white,
    onSecondary =  white,
    onBackground = black,
    onSurface = xanadu,
    onError = white
)

@Composable
fun HGPTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
