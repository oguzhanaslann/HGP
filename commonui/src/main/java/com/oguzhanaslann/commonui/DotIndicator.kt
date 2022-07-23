package com.oguzhanaslann.commonui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Suppress("LongParameterList")
fun PagerIndicator(
    modifier: Modifier = Modifier,
    currentSelectionIndex: Int,
    total: Int,
    selectedItemColor: Color = MaterialTheme.colors.primary,
    unselectedItemColor: Color = MaterialTheme.colors.onSurface,
    onDotClick: (Int) -> Unit = {},
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(total) {
            Indicator(
                isSelected = it == currentSelectionIndex,
                color = if (it == currentSelectionIndex) selectedItemColor else unselectedItemColor,
                onClick = { onDotClick(it) }
            )
        }
    }
}

@Composable
fun Indicator(
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit = {}
) {

    val width = animateDpAsState(targetValue = if (isSelected) 30.dp else 10.dp)

    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .clickable { onClick() }
            .background(color)

    )
}
