package org.mifos.mobile.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.feature.home.screens.HomeScreen

fun NavGraphBuilder.homeNavGraph(
    onNavigate: (HomeDestinations) -> Unit,
    callHelpline: () -> Unit,
    mailHelpline: () -> Unit
) {
    navigation(
        startDestination = HomeNavigation.HomeScreen.route,
        route = HomeNavigation.HomeBase.route,
    ) {
        homeRoute(
            onNavigate = onNavigate,
            callHelpline = callHelpline,
            mailHelpline = mailHelpline,
        )
    }
}

fun NavGraphBuilder.homeRoute(
    onNavigate: (HomeDestinations) -> Unit,
    callHelpline: () -> Unit,
    mailHelpline: () -> Unit
) {
    composable(
        route = HomeNavigation.HomeScreen.route,
    ) {
        HomeScreen(
            callHelpline = callHelpline,
            mailHelpline = mailHelpline,
            onNavigate = onNavigate
        )
    }
}

fun NavController.navigateToHomeScreen(navOptions: NavOptions? = null) {
    return this.navigate(HomeNavigation.HomeScreen.route, navOptions)
}