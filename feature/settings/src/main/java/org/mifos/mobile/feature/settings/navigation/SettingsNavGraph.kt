package org.mifos.mobile.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.feature.settings.SettingsScreen

fun NavController.navigateToSettings() {
    navigate(SettingsNavigation.SettingsBase.route)
}

fun NavGraphBuilder.settingsNavGraph(
    navController: NavController,
    navigateToLoginScreen: () -> Unit,
    changePassword: () -> Unit,
    changePasscode: (String) -> Unit,
    languageChanged: () -> Unit
) {
    navigation(
        startDestination = SettingsNavigation.SettingsScreen.route,
        route = SettingsNavigation.SettingsBase.route,
    ) {
        settingsScreenRoute(
            navigateBack = navController::popBackStack,
            navigateToLoginScreen = navigateToLoginScreen,
            changePassword = changePassword,
            changePasscode = changePasscode,
            languageChanged = languageChanged
        )
    }
}

fun NavGraphBuilder.settingsScreenRoute(
    navigateBack: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    changePassword: () -> Unit,
    changePasscode: (String) -> Unit,
    languageChanged: () -> Unit
) {
    composable(
        route = SettingsNavigation.SettingsScreen.route,
    ) {
        SettingsScreen(
            navigateBack = navigateBack,
            navigateToLoginScreen = navigateToLoginScreen,
            changePassword = changePassword,
            changePasscode = changePasscode,
            languageChanged = languageChanged
        )
    }
}