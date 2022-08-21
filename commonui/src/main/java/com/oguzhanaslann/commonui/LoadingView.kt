package com.oguzhanaslann.commonui

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieConstants
import com.oguzhanaslann.commonui.theme.HGPExtendedTheme
import com.oguzhanaslann.commonui.theme.LocalBlurColors


@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    iterations: Int = LottieConstants.IterateForever,
    isPlaying: Boolean = true,
    restartOnPlay: Boolean = false,
    blurColors: LocalBlurColors = HGPExtendedTheme.blurColors
) {

    Blur(blurColors = blurColors) {
        Box(modifier = modifier.fillMaxSize()) {
            LottieAnimationView(
                modifier = Modifier
                    .size(124.dp)
                    .align(Alignment.Center),
                iterations = iterations,
                isPlaying = isPlaying,
                restartOnPlay = restartOnPlay,
                animation = R.raw.loading_lottie,
            )
        }
    }
}

@Composable
fun Blur(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    blurColors: LocalBlurColors = HGPExtendedTheme.blurColors,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        color = blurColors.blur,
        shape = shape,
        border = border,
        elevation = elevation
    ) {
        content()
    }
}


@Composable
fun Pulsating(
    modifier : Modifier= Modifier,
    pulseFraction: Float = 1.5f,
    isPulsing : Boolean = true,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()

    val animation = tween<Float>(1500)
    val repeatMode = RepeatMode.Restart
    val initialValue = 1f


    val scale by infiniteTransition.animateFloat(
        initialValue = initialValue,
        targetValue = pulseFraction,
        animationSpec = infiniteRepeatable(
            animation = animation,
            repeatMode = repeatMode
        )
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = initialValue,
        targetValue =  0.0f,
        animationSpec = infiniteRepeatable(
            animation = animation,
            repeatMode = repeatMode
        )
    )

    Box(modifier = modifier
        .scale(if (isPulsing) scale else 1f)
        .alpha(if (isPulsing) alpha else 1f)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            content()
        }
    }
}
