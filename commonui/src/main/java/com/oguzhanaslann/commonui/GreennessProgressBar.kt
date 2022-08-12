package com.oguzhanaslann.commonui


import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.oguzhanaslann.commonui.theme.HGPExtendedTheme

sealed class GreennessProgress {
    object Bad : GreennessProgress()
    object Average : GreennessProgress()
    object Good : GreennessProgress()
    object Excellent : GreennessProgress()
    object Perfect : GreennessProgress()

    companion object {
        fun from(float: Float): GreennessProgress {
            return when (float) {
                in 0.0f..0.20f -> Bad
                in 0.21f..0.40f -> Average
                in 0.41f..0.60f -> Good
                in 0.61f..0.80f -> Excellent
                in 0.81f..1.0f -> Perfect
                else -> Perfect
            }
        }
    }
}

internal val GreennessProgress.ordinal: Int
    get() = when (this) {
        GreennessProgress.Bad -> 0
        GreennessProgress.Average -> 1
        GreennessProgress.Good -> 2
        GreennessProgress.Excellent -> 3
        GreennessProgress.Perfect -> 4
    }

internal val GreennessProgress.level: Int
    get() = this.ordinal + 1

val GreennessProgress.name
    get() = when (this) {
        GreennessProgress.Bad -> "Bad"
        GreennessProgress.Average -> "Average"
        GreennessProgress.Good -> "Good"
        GreennessProgress.Excellent -> "Excellent"
        GreennessProgress.Perfect -> "Perfect"
    }

@Composable
fun GreennessProgressbar(
    modifier: Modifier = Modifier,
    progress: GreennessProgress = GreennessProgress.Average,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(progress.level) {
            Spacer(modifier = Modifier.width(4.dp))
            val isLastStep = it == progress.ordinal
            GreennessStepView(
                modifier = Modifier.width(
                    if (isLastStep) 96.dp else 24.dp
                ),
                color = getStepBackgroundColor(it)
            ) {
                if (isLastStep) {
                    Text(
                        text = (progress.name),
                        modifier = Modifier.padding(horizontal = 12.dp),
                        style = MaterialTheme.typography.caption,
                        color = getStepTextColor(it)
                    )
                }
            }
        }
    }
}

@Composable
private fun getStepTextColor(it: Int) = when (it) {
    GreennessProgress.Bad.ordinal -> HGPExtendedTheme.colors.onProgressBad
    GreennessProgress.Average.ordinal -> HGPExtendedTheme.colors.onProgressAverage
    GreennessProgress.Good.ordinal -> HGPExtendedTheme.colors.onProgressGood
    GreennessProgress.Excellent.ordinal -> HGPExtendedTheme.colors.onProgressExcellent
    GreennessProgress.Perfect.ordinal -> HGPExtendedTheme.colors.onProgressPerfect
    else -> HGPExtendedTheme.colors.onProgressAverage
}

@Composable
private fun getStepBackgroundColor(it: Int) = when (it) {
    GreennessProgress.Bad.ordinal -> HGPExtendedTheme.colors.progressBad
    GreennessProgress.Average.ordinal -> HGPExtendedTheme.colors.progressAverage
    GreennessProgress.Good.ordinal -> HGPExtendedTheme.colors.progressGood
    GreennessProgress.Excellent.ordinal -> HGPExtendedTheme.colors.progressExcellent
    GreennessProgress.Perfect.ordinal -> HGPExtendedTheme.colors.progressPerfect
    else -> HGPExtendedTheme.colors.progressAverage
}

@Composable
fun GreennessStepView(
    modifier: Modifier = Modifier,
    shapes: Shape = MaterialTheme.shapes.small,
    color: Color = MaterialTheme.colors.primary,
    content: @Composable () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .defaultMinSize(minHeight = 24.dp, minWidth = 42.dp)
            .then(modifier),
        shape = shapes,
        color = color
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}
