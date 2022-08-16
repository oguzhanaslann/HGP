package com.oguzhanaslann.hgp.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.oguzhanaslann.commonui.toggle
import com.oguzhanaslann.hgp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainContentView(
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    content: @Composable () -> Unit = {},
    actionContent: @Composable () -> Unit = {}
) {
    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            HGPBottomAppBar(
                onDrawerClick = { scope.launch { scaffoldState.drawerState.toggle() } }
            )
        },
        drawerContent = {
            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp),
                onClick = { scope.launch { scaffoldState.drawerState.close() } },
                content = { Text("Close Drawer") }
            )
        },
        floatingActionButton = { actionContent() },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        content = { values ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(values)
            ) {
                content()
            }
        }
    )
}

@Composable
private fun HGPBottomAppBar(
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
