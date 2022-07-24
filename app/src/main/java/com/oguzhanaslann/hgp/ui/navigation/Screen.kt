package com.oguzhanaslann.hgp.ui.navigation

import androidx.navigation.NamedNavArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    object OnBoarding : Screen("${ROUTE_PREFIX}_onboarding")
    object Scanner : Screen("${ROUTE_PREFIX}_scanner")

    companion object {
        // route prefix
        const val ROUTE_PREFIX = "screen"
    }
}
