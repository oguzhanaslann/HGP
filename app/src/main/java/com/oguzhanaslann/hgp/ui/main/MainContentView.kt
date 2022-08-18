package com.oguzhanaslann.hgp.ui.main

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oguzhanaslann.camera.CameraViewModel
import com.oguzhanaslann.camera.ScanView
import com.oguzhanaslann.common.SearchType
import com.oguzhanaslann.commonui.emptyComposable
import com.oguzhanaslann.commonui.theme.white
import com.oguzhanaslann.commonui.toggle
import com.oguzhanaslann.hgp.R
import com.oguzhanaslann.navigation.MainContentScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class MainContentState(
    val scope: CoroutineScope,
    val scaffoldState: ScaffoldState,
    val navController: NavHostController
) {
    val startDestination = MainContentScreen.Camera.route
    var searchType by mutableStateOf<SearchType>(SearchType.CameraSearch.QRScanSearch)

    init {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.route) {
                MainContentScreen.Camera.route -> {
                    searchType = SearchType.CameraSearch.QRScanSearch
                }
            }
        }
    }

    var actionClickListener: (() -> Unit)? = null

    fun onActionClick() {
        actionClickListener?.invoke()
    }
}

@Composable
fun rememberMainContentState(
    scope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController()
) = remember(scope, scaffoldState, navController) {
    MainContentState(
        scope,
        scaffoldState,
        navController
    )
}


@Composable
fun MainContentView(
    modifier: Modifier = Modifier,
    state: MainContentState = rememberMainContentState()
) {
    DisposableEffect(key1 = Unit) {
        object : DisposableEffectResult {
            override fun dispose() {
                state.actionClickListener = null
            }
        }
    }

    Scaffold(
        modifier = modifier,
        scaffoldState = state.scaffoldState,
        bottomBar = {
            HGPBottomAppBar(
                onDrawerClick = { state.scope.launch { state.scaffoldState.drawerState.toggle() } }
            )
        },
        drawerContent = {
            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp),
                onClick = { state.scope.launch { state.scaffoldState.drawerState.close() } },
                content = { Text("Close Drawer") }
            )
        },
        floatingActionButton = {
            when (state.searchType) {
                is SearchType.CameraSearch.ImageSearch -> {
                    IconButton(onClick = state::onActionClick) {
                        Icon(
                            painterResource(id = com.oguzhanaslann.camera.R.drawable.ic_shutter),
                            contentDescription = "Shutter",
                            tint = white
                        )
                    }
                }
                else -> emptyComposable()
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        content = { values ->
            NavHost(
                navController = state.navController,
                startDestination = state.startDestination,
                modifier = Modifier.padding(values)
            ) {
                composable(MainContentScreen.Camera.route) {
                    val cameraViewModel: CameraViewModel = hiltViewModel()
                    val context = LocalContext.current
                    state.actionClickListener = {
                        cameraViewModel.onImageCapturing()
                        val executor = ContextCompat.getMainExecutor(context)
                        val outputFileOptions = ImageCapture.OutputFileOptions
                            .Builder(
                                File(context.cacheDir, "capture_${UUID.randomUUID()}.jpg")
                            )
                            .build()
                        cameraViewModel.imageCapture?.takePicture(
                            outputFileOptions,
                            executor,
                            object : ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                    cameraViewModel.onImageCapture(outputFileResults.savedUri)
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    cameraViewModel.onImageCaptureError(exception)
                                }
                            })
                    }
                    ScanView(
                        cameraViewModel = cameraViewModel,
                        onScanModeChanged = {
                            state.searchType = it
                            cameraViewModel.setScanType(it)
                        }
                    )
                }
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
