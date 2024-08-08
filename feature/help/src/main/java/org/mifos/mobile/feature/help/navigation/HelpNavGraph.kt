package org.mifos.mobile.feature.help.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.feature.help.HelpScreen
import org.mifos.mobile.feature.help.R

fun NavController.navigateToHelpScreen() {
    navigate(HelpNavigation.HelpScreen.route)
}

fun NavGraphBuilder.helpNavGraph(
    findLocations: () -> Unit,
    navigateBack: () -> Unit,
    callHelpline: () -> Unit,
    mailHelpline: () -> Unit
) {
    navigation(
        startDestination = HelpNavigation.HelpScreen.route,
        route = HelpNavigation.HelpBase.route,
    ) {
        helpScreenRoute(
            findLocations = findLocations,
            navigateBack = navigateBack,
            callHelpline = callHelpline,
            mailHelpline = mailHelpline
        )
    }
}

fun NavGraphBuilder.helpScreenRoute(
    findLocations: () -> Unit,
    navigateBack: () -> Unit,
    callHelpline: () -> Unit,
    mailHelpline: () -> Unit
) {
    composable(
        route = HelpNavigation.HelpScreen.route,
    ) {
        val context = LocalContext.current
        HelpScreen(
            callNow = callHelpline,
            leaveEmail = mailHelpline,
            findLocations = findLocations,
            navigateBack = navigateBack,
        )
    }
}

