package org.mifos.mobile.feature.notification.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.mifos.mobile.feature.notification.NotificationScreen

fun NavController.navigateToNotificationScreen() {
    navigate(NotificationNavigation.NotificationScreen.route)
}

fun NavGraphBuilder.notificationNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = NotificationNavigation.NotificationScreen.route,
        route = NotificationNavigation.NotificationBase.route,
    ) {
        notificationScreenRoute(
            navigateBack = navController::popBackStack,
        )
    }
}

fun NavGraphBuilder.notificationScreenRoute(
    navigateBack: () -> Unit,
) {
    composable(
        route = NotificationNavigation.NotificationScreen.route,
    ) {
        NotificationScreen(
            navigateBack = navigateBack,
        )
    }
}