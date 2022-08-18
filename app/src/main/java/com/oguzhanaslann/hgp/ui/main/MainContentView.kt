package com.oguzhanaslann.hgp.ui.main

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.oguzhanaslann.commonui.theme.*
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

    fun startBarcodeScan() {
        // TODO("Not yet implemented")
    }

    fun startVoiceSearch() {
        // TODO("Not yet implemented")
    }

    fun startTextSearch() {
        // TODO("Not yet implemented")
    }

    fun startVisualSearch() {
        // TODO("Not yet implemented")
    }

    fun goToPrivacyPolicy() {
        // TODO("Not yet implemented")
    }

    fun goToContactUs() {
        // TODO("Not yet implemented")
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
        drawerShape = MaterialTheme.shapes.large.copy(
            topStart = CornerSize(0.dp),
            bottomStart = CornerSize(0.dp)
        ),
        drawerContent = {
            HGPDrawer(
                onBarcodeScanClicked = { state.startBarcodeScan() },
                onVoiceSearchClicked = { state.startVoiceSearch() },
                onTextSearchClicked = { state.startTextSearch() },
                onVisualSearchClicked = { state.startVisualSearch() },
                onPrivacyPolicyClicked = { state.goToPrivacyPolicy() },
                onContactUsClicked = {  state.goToContactUs() },
                onShareClicked = { }
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
fun HGPDrawer(
    modifier: Modifier = Modifier,
    onBarcodeScanClicked : () -> Unit = {},
    onVoiceSearchClicked : () -> Unit = {},
    onTextSearchClicked : () -> Unit = {},
    onVisualSearchClicked : () -> Unit = {},
    onPrivacyPolicyClicked : () -> Unit = {},
    onContactUsClicked : () -> Unit = {},
    onShareClicked : () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(0.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.primary
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .size(144.dp)
                            .padding(top = xlargeContentPadding),
                        painter = painterResource(id = R.drawable.ic_hgp_logo),
                        contentDescription = "HGP Logo",
                    )

                    Text(
                        modifier = Modifier.padding(vertical = defaultContentPadding),
                        text = stringResource(id = R.string.app_name_long),
                        style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold)
                    )

                }
            }

            Column {

                Text(
                    modifier = Modifier
                        .padding(top = smallContentPadding)
                        .padding(horizontal = largeContentPadding),
                    text = "Search",
                    style = MaterialTheme.typography.subtitle1,
                )

                DrawerItem(
                    painter = painterResource(id = com.oguzhanaslann.commonui.R.drawable.ic_capture),
                    text = stringResource(R.string.barcode_scan),
                    onClick = onBarcodeScanClicked
                )

                DrawerItem(
                    painter = painterResource(id = com.oguzhanaslann.commonui.R.drawable.ic_microphone),
                    text = stringResource(R.string.voice_search),
                    onClick = onVoiceSearchClicked
                )


                DrawerItem(
                    painter = painterResource(id = com.oguzhanaslann.commonui.R.drawable.ic_pencil),
                    text = stringResource(R.string.text_search),
                    onClick = onTextSearchClicked
                )

                DrawerItem(
                    painter = painterResource(id = com.oguzhanaslann.commonui.R.drawable.ic_camera),
                    text = stringResource(R.string.visual_search),
                    onClick =  onVisualSearchClicked
                )
            }

            Divider(modifier = Modifier.fillMaxWidth(0.90f))

            Column {
                Text(
                    modifier = Modifier
                        .padding(top = smallContentPadding)
                        .padding(horizontal = largeContentPadding),
                    text = "General",
                    style = MaterialTheme.typography.subtitle1,
                )

                DrawerItem(
                    painter = painterResource(id = com.oguzhanaslann.commonui.R.drawable.ic_info),
                    text = stringResource(R.string.privacy_policy),
                    onClick = onPrivacyPolicyClicked
                )

                DrawerItem(
                    painter = painterResource(id = com.oguzhanaslann.commonui.R.drawable.ic_mail),
                    text = stringResource(R.string.contact_us),
                    onClick = onContactUsClicked
                )

                DrawerItem(
                    painter = painterResource(id = com.oguzhanaslann.commonui.R.drawable.ic_social),
                    text = stringResource(R.string.share),
                    onClick = onShareClicked
                )
            }
        }
    }
}

@Composable
fun DrawerItem(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = null,
    text: String,
    contentColor: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .padding(vertical = defaultContentPadding)
                .padding(start = largeContentPadding),
            painter = painter,
            contentDescription = contentDescription,
            tint = contentColor,
        )

        Text(
            modifier = Modifier.padding(horizontal = largeContentPadding),
            text = text,
            style = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
            color = contentColor
        )
    }
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
