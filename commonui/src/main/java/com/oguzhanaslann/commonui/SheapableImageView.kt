package com.oguzhanaslann.commonui

import androidx.compose.foundation.Image
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun SheapableImageView(
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    shape: Shape = MaterialTheme.shapes.small,
    placeHolderColor: Color = Color.Black.copy(.2f),
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = if (painter == null) placeHolderColor else placeHolderColor.copy(0f)
    ) {
        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = contentScale
            )
        }
    }
}


@Composable
fun SheapableImageView(
    url: String ?= null,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    placeHolderColor: Color = Color.Black.copy(.2f),
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = if (url == null) placeHolderColor else placeHolderColor.copy(0f)
    ) {
        if (url != null) {
            AsyncImage(
                model = url,
                contentDescription = contentDescription,
                contentScale = contentScale
            )
        }
    }
}
