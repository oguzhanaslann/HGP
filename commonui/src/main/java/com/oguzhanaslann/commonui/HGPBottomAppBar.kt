package com.oguzhanaslann.commonui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun HGPBottomAppBar(
    modifier: Modifier = Modifier,
    onDrawerClick: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
) {
    BottomAppBar(
        modifier = modifier,
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
            IconButton(onClick = onDrawerClick) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.menu_button_description),
                )
            }
        }
        Spacer(Modifier.weight(1f, true))

        IconButton(onClick = onSearchClicked) {
            Icon(
                Icons.Filled.Search,
                contentDescription = stringResource(R.string.search_button_description),
            )
        }
    }
}
