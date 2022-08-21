package com.oguzhanaslann.commonui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieConstants
import com.oguzhanaslann.commonui.theme.HGPExtendedTheme
import com.oguzhanaslann.commonui.theme.LocalBlurColors

@Composable
fun ErrorView(
    modifier : Modifier = Modifier,
    iterations: Int = LottieConstants.IterateForever,
    isPlaying: Boolean = true,
    restartOnPlay: Boolean = false,
    blurColors : LocalBlurColors = HGPExtendedTheme.blurColors
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
