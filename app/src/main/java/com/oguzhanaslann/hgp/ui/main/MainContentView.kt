package com.oguzhanaslann.hgp.ui.main

import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.oguzhanaslann.voice.VoiceView
import com.oguzhanaslann.voice.VoiceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

private const val TAG = "MainContentView"

class MainContentState(
    val scope: CoroutineScope,
    val scaffoldState: ScaffoldState,
    val navController: NavHostController
) {
    val startDestination = MainContentScreen.Camera.route

    fun startBarcodeScan(closeDrawer: Boolean = true) {
        navController.navigate(MainContentScreen.Camera.route)
        closeDrawerBy(closeDrawer)
    }

    fun startVoiceSearch(closeDrawer: Boolean = true) {
        navController.navigate(MainContentScreen.VoiceSearch.route)
        closeDrawerBy(closeDrawer)
    }

    fun startTextSearch(closeDrawer: Boolean = true) {
        // TODO("Not yet implemented")
        closeDrawerBy(closeDrawer)
    }

    fun startVisualSearch(closeDrawer: Boolean = true) {
        navController.navigate(MainContentScreen.Camera.route)
        closeDrawerBy(closeDrawer)
    }

    private fun closeDrawerBy(closeDrawer: Boolean) {
        if (closeDrawer) {
            scope.launch { scaffoldState.drawerState.close() }
        }
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
    state: MainContentState = rememberMainContentState(),
) {
    NavHost(
        navController = state.navController,
        startDestination = state.startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(MainContentScreen.Camera.route) {
            val cameraViewModel: CameraViewModel = hiltViewModel()
            ScanView(
                cameraViewModel = cameraViewModel,
                onTextSearchClicked = {
                    state.startTextSearch()
                },
                onVoiceSearchClicked = {
                    state.startVoiceSearch()
                },
                onPrivacyPolicyClicked = {
                    state.goToPrivacyPolicy()
                },
                onContactUsClicked = {
                    state.goToContactUs()
                },
                onShareClicked = {
                    // TODO("Not yet implemented")
                }
            )
        }

        composable(MainContentScreen.VoiceSearch.route) {
            VoiceView(
                onTextSearchClicked = {
                    state.startTextSearch()
                },

                onPrivacyPolicyClicked = {
                    state.goToPrivacyPolicy()
                },
                onContactUsClicked = {
                    state.goToContactUs()
                },
                onShareClicked = {
                    // TODO("Not yet implemented")
                },
                voiceViewModel = VoiceViewModel(),
                onBarcodeScanClicked = {
                    state.startBarcodeScan()
                },
                onVisualSearchClicked = {
                    state.startVisualSearch()
                }
            )
        }
    }
}
