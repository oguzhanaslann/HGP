package com.oguzhanaslann.commonui.base

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel

/**
 *  you must set viewModel first before using composable function
 * */
interface SearchContent<T : ViewModel> {
    fun setViewModel(viewModel: T)
    fun getSearchContent(): @Composable () -> Unit
    fun getSearchAction(): @Composable () -> Unit
}
