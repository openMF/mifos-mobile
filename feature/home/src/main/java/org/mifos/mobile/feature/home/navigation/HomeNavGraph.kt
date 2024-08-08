package org.mifos.mobile.feature.home.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.feature.home.R
import org.mifos.mobile.feature.home.screens.HomeScreen
import org.mifos.mobile.feature.home.viewmodel.HomeCardItem

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
