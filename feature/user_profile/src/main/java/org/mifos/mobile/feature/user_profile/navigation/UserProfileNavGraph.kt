package org.mifos.mobile.feature.user_profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.feature.user_profile.screens.UserProfileScreen

fun NavController.navigateToUserProfile() {
    navigate(UserProfileNavigation.UserProfileBase.route)
}

fun NavGraphBuilder.userProfileNavGraph(
    navController: NavController,
    navigateToChangePassword: () -> Unit
) {
    navigation(
        startDestination = UserProfileNavigation.UserProfileScreen.route,
        route = UserProfileNavigation.UserProfileBase.route,
    ) {
        userProfileRoute(
            navigateBack = navController::popBackStack,
            navigateToChangePassword = navigateToChangePassword
        )
    }
}

fun NavGraphBuilder.userProfileRoute(
    navigateBack: () -> Unit,
    navigateToChangePassword: () -> Unit
) {
    composable(
        route = UserProfileNavigation.UserProfileScreen.route,
    ) {
        UserProfileScreen(
            navigateBack = navigateBack,
            changePassword = navigateToChangePassword
        )
    }
}