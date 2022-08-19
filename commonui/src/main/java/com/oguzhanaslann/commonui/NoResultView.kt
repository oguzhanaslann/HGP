package com.oguzhanaslann.commonui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieConstants
import com.oguzhanaslann.commonui.theme.HGPExtendedTheme
import com.oguzhanaslann.commonui.theme.LocalBlurColors
import com.oguzhanaslann.commonui.theme.defaultContentPadding

@Composable
fun NoResultView(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.no_result),
    iterations: Int = LottieConstants.IterateForever,
    isPlaying: Boolean = true,
    restartOnPlay: Boolean = false,
    blurColors: LocalBlurColors = HGPExtendedTheme.blurColors,
) {

    Surface(
        color = blurColors.blur,
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieAnimationView(
                    modifier = Modifier
                        .size(256.dp),
                    iterations = iterations,
                    isPlaying = isPlaying,
                    restartOnPlay = restartOnPlay,
                    animation = R.raw.no_result_lottie,
                )

                Text(
                    modifier = Modifier.padding(defaultContentPadding),
                    text = text,
                    style = MaterialTheme.typography.h5,
                    color = blurColors.onBlur,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
