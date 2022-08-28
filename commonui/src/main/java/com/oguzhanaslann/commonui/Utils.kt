package com.oguzhanaslann.commonui

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.DrawerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

suspend fun DrawerState.toggle() {
    if (isOpen) close() else open()
}

val emptyComposable: @Composable () -> Unit
    get() = {}

val Shapes.drawerShape
    @Composable
    get() = MaterialTheme.shapes.large.copy(
        topStart = CornerSize(0.dp),
        bottomStart = CornerSize(0.dp)
    )
