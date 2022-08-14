package com.oguzhanaslann.camera

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzhanaslann.commonui.ShapeableImageView
import com.oguzhanaslann.commonui.theme.*

@Composable
fun ImagePreviewView(
    modifier: Modifier = Modifier,
    previewUri: String? = null,
    onCancel: () -> Unit = {},
    onAnalyzeClick: () -> Unit = {}
) {

    var showActionsState by remember { mutableStateOf(true) }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(
                key1 = "single_tap_input_for_image_preview",
            ) {
                detectTapGestures(
                    onTap = { showActionsState = !showActionsState }
                )
            }
    ) {
        Box {
            ShapeableImageView(
                url = previewUri,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                shape = RoundedCornerShape(0.dp),
                contentScale = ContentScale.Crop
            )

            AnimatedVisibility(
                visible = showActionsState,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
               Box(
                   modifier = Modifier.fillMaxSize()
               ) {
                   CancelView(
                       modifier = Modifier
                           .align(Alignment.TopStart)
                           .padding(start = xlargeContentPadding)
                           .padding(top = xlargeContentPadding),
                       onClick = onCancel
                   )

                   AnalyzeView(
                       modifier = Modifier
                           .align(Alignment.BottomCenter)
                           .padding(bottom = xxlargeContentPadding)
                           .fillMaxWidth(0.80f),
                       onAnalyzeClick = onAnalyzeClick
                   )
               }
            }

        }
    }
}

@Composable
fun AnalyzeView(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    borderColors: SurfaceBorderColors = HGPExtendedTheme.surfaceBorderColors,
    onAnalyzeClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .border(
                color = borderColors.surfaceBorder.copy(alpha = 0.95f),
                width = 1.dp,
                shape = shape
            ),
        shape = shape,
        color = MaterialTheme.colors.surface.copy(alpha = 0.95f)
    ) {
        Column(
            modifier = Modifier.padding(defaultContentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(defaultContentPadding)
        ) {
            Icon(
                painter = painterResource(id = com.oguzhanaslann.commonui.R.drawable.ic_capture),
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface
            )

            Text(
                text = "Scan image for products",
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.onSurface
            )

            Image(
                painter = painterResource(id = R.drawable.ic_scan_capture),
                contentDescription = null
            )

            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary,
                ),
                onClick = { onAnalyzeClick() },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        painter = painterResource(id = com.oguzhanaslann.commonui.R.drawable.ic_capture),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Analyze",
                        color = MaterialTheme.colors.onSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun CancelView(
    modifier: Modifier = Modifier,
    contentColors: LocalBlurColors = HGPExtendedTheme.blurColors,
    shape: Shape = MaterialTheme.shapes.small,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .clickable { onClick() },
        shape = shape,
        color = contentColors.blur
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_close_24),
                tint = contentColors.onBlur,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.cancel),
                color = contentColors.onBlur,
                style = MaterialTheme.typography.overline.copy(fontSize = 12.sp),
            )
        }
    }
}
