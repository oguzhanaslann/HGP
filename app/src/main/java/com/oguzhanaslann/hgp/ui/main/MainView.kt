package com.oguzhanaslann.hgp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oguzhanaslann.camera.CameraViewModel
import com.oguzhanaslann.camera.ScanView
import com.oguzhanaslann.hgp.MainViewModel
import com.oguzhanaslann.navigation.ComposeNavigator
import com.oguzhanaslann.navigation.Screen
import com.oguzhanaslann.onboarding.onboarding.OnBoardingView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun rememberMainScreenState(
    viewModel: MainViewModel = viewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navigator: ComposeNavigator
) = remember(viewModel, coroutineScope) {
    MainScreenState(viewModel, coroutineScope, navigator)
}

class MainScreenState(
    private val mainViewModel: MainViewModel,
    coroutineScope: CoroutineScope,
    private val navigator: ComposeNavigator
) {
    init {
        coroutineScope.launch {
            mainViewModel.isDoneOnBoarding.collectLatest {
                if (it) {
                    navigator.navigateAndClearBackStack(Screen.MainContent.route)
                }
            }
        }
    }

    fun skipOnBoard() {
        mainViewModel.onSkipOnBoarding()
    }
}


@Composable
fun MainView(
    navigator: ComposeNavigator,
    mainViewModel: MainViewModel = viewModel(),
    navHostState: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {

    val mainScreenState = rememberMainScreenState(
        viewModel = mainViewModel,
        coroutineScope = coroutineScope,
        navigator = navigator
    )

    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        navController = navHostState,
//        startDestination = Screen.OnBoarding.route
        startDestination = Screen.MainContent.route
    ) {
        composable(Screen.OnBoarding.route) {
            OnBoardingView(
                onSkip = mainScreenState::skipOnBoard
            )
        }

        composable(Screen.MainContent.route) {
            val content by mainViewModel.searchContent.collectAsState()
            val cameraViewModel: CameraViewModel = hiltViewModel()
            content.setViewModel(cameraViewModel)

            MainContentView(
                modifier = Modifier.fillMaxSize(),
                content = {
                    val searchContent: @Composable () -> Unit = content.getSearchContent()
                    searchContent()
                },
                actionContent = {
                    val action: @Composable () -> Unit = content.getSearchAction()
                    action()
                }
            )
        }
    }
}

enum class SearchType {
    TextSearch,
    VoiceSearch,
    ImageSearch,
    QRScanSearch
}
