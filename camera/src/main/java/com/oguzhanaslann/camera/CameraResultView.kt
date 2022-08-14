package com.oguzhanaslann.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzhanaslann.commonui.GreennessProgress
import com.oguzhanaslann.commonui.GreennessProgressbar
import com.oguzhanaslann.commonui.ShapeableImageView
import com.oguzhanaslann.commonui.theme.HGPExtendedTheme
import com.oguzhanaslann.commonui.theme.contentPadding
import com.oguzhanaslann.commonui.theme.largeContentPadding

sealed class Statistic(val name: String)
class BinaryStatistic(name: String, val isExists: Boolean) : Statistic(name)
class LevelStatistic(name: String, val level: Level) : Statistic(name)

data class Ratio(val numerator: Int, val denominator: Int) {
    init {
        require(denominator >= 0) { "denominator must be greater than or equal to 0" }
    }

    val value: Float get() = numerator.toFloat() / denominator.toFloat()

    override fun toString(): String {
        return "$numerator/$denominator"
    }
}

data class ProductScanResult(
    val productName: String,
    val productImage: String,
    val statistics: List<Statistic>,
    val greennessRatio: Ratio,
)

@Composable
fun CameraResultView(
    productScanResult: ProductScanResult,
    modifier: Modifier = Modifier,
    onGoBackClicked: () -> Unit = {},
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HGPExtendedTheme.blurColors.blur)
            .then(modifier)
    ) {
        Row(
            modifier = Modifier
                .padding(top = contentPadding)
                .fillMaxWidth()
                .padding(horizontal = contentPadding)
        ) {
            GoBackView(
                modifier = Modifier.padding(top = contentPadding, start = contentPadding),
                onClick = { onGoBackClicked() }
            )
        }

        Spacer(modifier = Modifier.height(contentPadding))


        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = largeContentPadding)
                .fillMaxHeight()
                .padding(bottom = largeContentPadding),
            shape = MaterialTheme.shapes.large,
        ) {

            Column(
                modifier = Modifier.padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                ShapeableImageView(
                    url = productScanResult.productImage,
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    placeHolderColor = Color.Gray,
                )

                Spacer(modifier = Modifier.height(contentPadding))

                Text(
                    style = MaterialTheme.typography.subtitle2,
                    text = productScanResult.productName,
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth(0.90f)
                        .padding(top = contentPadding)
                )

                LazyColumn(
                    modifier = Modifier
                        .padding(top = contentPadding)
                        .padding(horizontal = contentPadding),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(productScanResult.statistics) { statistic ->
                        when (statistic) {
                            is BinaryStatistic -> BinaryStatsView(
                                text = statistic.name,
                                isExists = statistic.isExists
                            )
                            is LevelStatistic -> LevelStatisticView(
                                text = statistic.name,
                                level = statistic.level
                            )
                        }
                    }

                    item {
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth(0.90f)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = contentPadding),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(R.string.greenness_point),
                                style = MaterialTheme.typography.subtitle1.copy(fontSize = 14.sp)
                            )

                            Text(
                                text = productScanResult.greennessRatio.toString(),
                                style = MaterialTheme.typography.subtitle1.copy(fontSize = 14.sp)
                            )
                        }

                        GreennessProgressbar(
                            modifier = Modifier.padding(top = contentPadding),
                            progress = GreennessProgress.from(productScanResult.greennessRatio.value)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LevelStatisticView(
    modifier: Modifier = Modifier,
    text: String,
    level: Level
) {
    StatContent(modifier = modifier, text = text) {
        LevelView(text = level.name, level = level)
    }
}

@Composable
private fun GoBackView(
    modifier: Modifier = Modifier,
    contentColor : Color = HGPExtendedTheme.blurColors.onBlur,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
            tint = contentColor,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.go_back),
            color = contentColor,
            style = MaterialTheme.typography.overline.copy(fontSize = 12.sp),
        )

    }
}

@Composable
fun BinaryStatsView(
    modifier: Modifier = Modifier,
    text: String,
    isExists: Boolean,
    textStyle: TextStyle = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
) {
    StatContent(modifier = modifier, text = text, textStyle = textStyle) {
        Image(
            painter = if (isExists) painterResource(id = R.drawable.ic_approved)
            else painterResource(id = R.drawable.ic_disapproved),
            contentDescription = null
        )
    }

}


@Composable
fun StatContent(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
    content: @Composable () -> Unit = {},
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = text, style = textStyle)
        content()
    }
}

@Composable
fun LevelView(
    modifier: Modifier = Modifier,
    text: String,
    level: Level,
    style: TextStyle = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
) {
    Text(
        style = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
        text = "Medium",
        color = when (level) {
            Level.Low -> MaterialTheme.colors.primary
            Level.Medium -> MaterialTheme.colors.secondary
            Level.High -> MaterialTheme.colors.error
        }
    )
}

enum class Level {
    Low,
    Medium,
    High
}
