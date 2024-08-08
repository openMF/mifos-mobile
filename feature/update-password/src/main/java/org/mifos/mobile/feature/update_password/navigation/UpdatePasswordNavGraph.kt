package org.mifos.mobile.feature.update_password.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.feature.update_password.UpdatePasswordScreen

fun NavController.navigateToUpdatePassword() {
    navigate(UpdatePasswordNavigation.UpdatePasswordBase.route)
}

fun NavGraphBuilder.updatePasswordNavGraph(
    navController: NavController,
) {
    navigation(
        startDestination = UpdatePasswordNavigation.UpdatePasswordScreen.route,
        route = UpdatePasswordNavigation.UpdatePasswordBase.route,
    ) {
        updatePasswordRoute(
            navigateBack = navController::popBackStack,
        )
    }
}

fun NavGraphBuilder.updatePasswordRoute(
    navigateBack: () -> Unit,
) {
    composable(
        route = UpdatePasswordNavigation.UpdatePasswordScreen.route,
    ) {
        UpdatePasswordScreen(
            navigateBack = navigateBack,
        )
    }
}