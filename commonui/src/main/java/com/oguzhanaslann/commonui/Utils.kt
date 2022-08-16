package com.oguzhanaslann.commonui

import androidx.compose.material.DrawerState
import androidx.compose.runtime.Composable

suspend fun DrawerState.toggle() {
    if (isOpen) close() else open()
}

val emptyComposable: @Composable () -> Unit
    get() = {}
