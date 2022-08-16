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
