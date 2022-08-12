package com.oguzhanaslann.commonui

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.oguzhanaslann.commonui.theme.HGPExtendedTheme
import com.oguzhanaslann.commonui.theme.LocalBlurColors
import com.oguzhanaslann.commonui.theme.black


@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    iterations: Int = LottieConstants.IterateForever,
    isPlaying: Boolean = true,
    restartOnPlay: Boolean = false,
    blurColors: LocalBlurColors = HGPExtendedTheme.blurColors
) {

    Surface(
        color = blurColors.blur,
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            LoadingAnimation(
                modifier = Modifier.align(Alignment.Center),
                iterations = iterations,
                isPlaying = isPlaying,
                restartOnPlay = restartOnPlay
            )
        }
    }
}

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    @RawRes animation: Int = R.raw.loading_lottie,
    iterations: Int = LottieConstants.IterateForever,
    isPlaying: Boolean = true,
    restartOnPlay: Boolean = false,
) {

    val speed by remember {
        mutableStateOf(1f)
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(animation)
    )

    val progress by animateLottieCompositionAsState(
        // pass the composition created above
        composition,

        // Iterates Forever
        iterations = iterations,

        // pass isPlaying we created above,
        // changing isPlaying will recompose
        // Lottie and pause/play
        isPlaying = isPlaying,

        // pass speed we created above,
        // changing speed will increase Lottie
        speed = speed,

        // this makes animation to restart
        // when paused and play
        // pass false to continue the animation
        // at which is was paused
        restartOnPlay = restartOnPlay

    )

    LottieAnimation(
        modifier = Modifier
            .size(124.dp)
            .then(modifier),
        composition = composition,
        progress = progress
    )
}
