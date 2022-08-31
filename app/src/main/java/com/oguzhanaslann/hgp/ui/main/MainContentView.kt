package com.oguzhanaslann.hgp.ui.main

import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oguzhanaslann.camera.CameraViewModel
import com.oguzhanaslann.camera.ScanView
import com.oguzhanaslann.navigation.MainContentScreen
import com.oguzhanaslann.textsearch.TextView
import com.oguzhanaslann.voice.VoiceView
import com.oguzhanaslann.voice.VoiceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TAG = "MainContentView"

class MainContentState(
    val scope: CoroutineScope,
    val scaffoldState: ScaffoldState,
    val navController: NavHostController
) {
    val startDestination = MainContentScreen.VoiceSearch.route

    fun startBarcodeScan(closeDrawer: Boolean = true) {
        navController.navigate(MainContentScreen.Camera.route)
        closeDrawerBy(closeDrawer)
    }

    fun startVoiceSearch(closeDrawer: Boolean = true) {
        navController.navigate(MainContentScreen.VoiceSearch.route)
        closeDrawerBy(closeDrawer)
    }

    fun startTextSearch(closeDrawer: Boolean = true) {
        navController.navigate(MainContentScreen.TextSearch.route)
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

        composable(MainContentScreen.TextSearch.route) {
            TextView()
        }
    }
}
