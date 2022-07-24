package com.oguzhanaslann.hgp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.oguzhanaslann.commonui.theme.HGPTheme
import com.oguzhanaslann.hgp.ui.main.MainView
import com.oguzhanaslann.navigation.ComposeNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var navigator: ComposeNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                (mainViewModel.isInitializing.value == true)
            }
        }
        super.onCreate(savedInstanceState)
        mainViewModel.getIsDoneOnboarding()


        setContent {
            val navState = rememberNavController()

            LaunchedEffect(Unit) {
                navigator.handleNavigationCommands(navState)
            }

            HGPTheme {
                MainView(
                    navigator = navigator,
                    navHostState = navState,
                    mainViewModel = mainViewModel
                )
            }
        }
    }

}
