package com.oguzhanaslann.commonui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = spanishGreen,
    primaryVariant = spanishGreen,
    secondary = bronze,
    secondaryVariant = bronze,
    background = arsenic,
    surface = antiFlashWhite,
    error = bronze,
    onPrimary = white,
    onSecondary = white,
    onBackground = white,
    onSurface = black,
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
    onSecondary = white,
    onBackground = black,
    onSurface = black,
    onError = white
)

@Immutable
data class ProgressColors(
    val progressBad: Color,
    val onProgressBad : Color,
    val progressAverage: Color,
    val onProgressAverage : Color,
    val progressGood: Color,
    val onProgressGood : Color,
    val progressExcellent: Color,
    val onProgressExcellent : Color,
    val progressPerfect: Color,
    val onProgressPerfect : Color
)

val LocalProgressColors = staticCompositionLocalOf {
    ProgressColors(
        progressBad = Color.Unspecified,
        onProgressBad = Color.Unspecified,
        progressAverage = Color.Unspecified,
        onProgressAverage = Color.Unspecified,
        progressGood = Color.Unspecified,
        onProgressGood = Color.Unspecified,
        progressExcellent = Color.Unspecified,
        onProgressExcellent = Color.Unspecified,
        progressPerfect = Color.Unspecified,
        onProgressPerfect = Color.Unspecified
    )
}

@Immutable
data class LocalBlurColors(
    val blur: Color,
    val onBlur : Color
)


val LocalBlurColorsComposer = staticCompositionLocalOf {
    LocalBlurColors(
        blur = Color.Unspecified,
        onBlur = Color.Unspecified
    )
}

@Composable
fun HGPTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val progressColors = ProgressColors(
        progressBad = Color(0xFFA7F1C1),
        onProgressBad = Color(0xFF2E705A),
        progressAverage = Color(0xFF65E793),
        onProgressAverage = Color(0xFF2E705A),
        progressGood = Color(0xFF23DC64),
        onProgressGood = white,
        progressExcellent = Color(0xFF189A46),
        onProgressExcellent = white,
        progressPerfect = Color(0xFF08782F),
        onProgressPerfect = white
    )

    val blurColors = LocalBlurColors(
        blur = black.copy(alpha = 0.7f),
        onBlur = white
    )

    CompositionLocalProvider(
        LocalProgressColors provides progressColors,
        LocalBlurColorsComposer provides blurColors
    ) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}


object HGPExtendedTheme {
    val colors: ProgressColors
        @Composable
        get() = LocalProgressColors.current

    val blurColors: LocalBlurColors
        @Composable
        get() = LocalBlurColorsComposer.current
}
