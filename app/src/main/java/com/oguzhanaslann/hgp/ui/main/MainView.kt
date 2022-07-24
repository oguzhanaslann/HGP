package com.oguzhanaslann.hgp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oguzhanaslann.hgp.MainViewModel
import com.oguzhanaslann.hgp.ui.navigation.ComposeNavigator
import com.oguzhanaslann.hgp.ui.navigation.Screen
import com.oguzhanaslann.onboarding.onboarding.OnBoardingView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun rememberMainScreenState(
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel = viewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navigator: ComposeNavigator
) = remember(navController, viewModel, coroutineScope) {
    MainScreenState(navController, viewModel, coroutineScope, navigator)
}

class MainScreenState(
    private val navController: NavHostController,
    private val mainViewModel: MainViewModel,
    coroutineScope: CoroutineScope,
    private val navigator: ComposeNavigator
) {
    init {
        coroutineScope.launch {
            mainViewModel.isDoneOnBoarding.collectLatest {
                if (it) {
                    navigator.navigateAndClearBackStack(Screen.Scanner.route)
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
        navController = navHostState,
        viewModel = mainViewModel,
        coroutineScope = coroutineScope,
        navigator = navigator
    )

    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        navController = navHostState,
        startDestination = Screen.OnBoarding.route
    ) {
        composable(Screen.OnBoarding.route) {
            OnBoardingView(
                onSkip = mainScreenState::skipOnBoard
            )
        }

        composable(Screen.Scanner.route) {
            Greeting("Scanner")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
