package com.oguzhanaslann.hgp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.oguzhanaslann.onboarding.onboarding.OnBoardingView
import com.oguzhanaslann.commonui.theme.HGPTheme

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                (mainViewModel.isInitializing.value == true)
            }
        }
        super.onCreate(savedInstanceState)
        mainViewModel.getIsDoneOnboarding()


        setContent {
            val isDoneOnBoarding = mainViewModel.isDoneOnBoarding.collectAsState()

            HGPTheme {
                // A surface container using the 'background' color from the theme
                MainView(
                    isDoneOnBoarding = isDoneOnBoarding.value,
                    onSkipOnBoarding = mainViewModel::onSkipOnBoarding,
                )
            }
        }
    }

}

@Composable
private fun MainView(
    isDoneOnBoarding: Boolean,
    onSkipOnBoarding: () -> Unit = {}
) {
    when {
        !isDoneOnBoarding -> OnBoardingView(
            onSkip = onSkipOnBoarding,
        )
        else -> {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                Greeting("Android")
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
