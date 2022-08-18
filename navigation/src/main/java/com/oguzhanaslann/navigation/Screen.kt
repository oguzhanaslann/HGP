package com.oguzhanaslann.navigation

import androidx.navigation.NamedNavArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    object OnBoarding : Screen("${ROUTE_PREFIX}_onboarding")
    object MainContent : Screen("${ROUTE_PREFIX}_main_content")

    companion object {
        // route prefix
        const val ROUTE_PREFIX = "screen"
    }
}


sealed class MainContentScreen(
    route: String,
    navArguments: List<NamedNavArgument> = emptyList()
) : Screen(route, navArguments) {
    object Camera : MainContentScreen("${ROUTE_PREFIX}_camera")
    object TextSearch : MainContentScreen("${ROUTE_PREFIX}_text_search")
    object VoiceSearch : MainContentScreen("${ROUTE_PREFIX}_voice_search")
}
