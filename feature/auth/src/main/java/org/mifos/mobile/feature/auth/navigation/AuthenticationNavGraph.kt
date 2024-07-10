package org.mifos.mobile.feature.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.feature.auth.login.screens.LoginScreen
import org.mifos.mobile.feature.auth.navigation.AuthenticationRoute.AUTH_NAVIGATION_ROUTE
import org.mifos.mobile.feature.auth.registration.screens.RegistrationScreen
import org.mifos.mobile.feature.auth.registration.screens.RegistrationVerificationScreen

fun NavGraphBuilder.authenticationNavGraph(
    startDestination: String,
    navController: NavHostController,
    navigateBack: () -> Unit,
    navigateToPasscodeScreen: () -> Unit
) {
    navigation(
        startDestination = startDestination,
        route = AUTH_NAVIGATION_ROUTE
    ) {
        loginRoute(
            navigateToRegisterScreen = { navController.navigate(AuthenticationNavigation.Registration.route) },
            navigateToPasscodeScreen = navigateToPasscodeScreen
        )

        registrationRoute(
            navigateBack = navigateBack,
            onRegistered = { navController.navigate(AuthenticationNavigation.RegistrationVerification.route) }
        )

        registrationVerificationRoute(
            navigateBack = { navController.popBackStack() },
            onRegistrationVerified = { navController.navigate(AuthenticationNavigation.Login.route) }
        )
    }
}

fun NavGraphBuilder.loginRoute(
    navigateToRegisterScreen: () -> Unit,
    navigateToPasscodeScreen: () -> Unit
) {
    composable(route = AuthenticationNavigation.Login.route) {
        LoginScreen(
            navigateToRegisterScreen = navigateToRegisterScreen,
            navigateToPasscodeScreen = navigateToPasscodeScreen,
        )
    }
}


fun NavGraphBuilder.registrationRoute(
    navigateBack: () -> Unit,
    onRegistered: () -> Unit
) {
    composable(route = AuthenticationNavigation.Registration.route){
        RegistrationScreen(
            onVerified = onRegistered,
            navigateBack = navigateBack
        )
    }
}

fun NavGraphBuilder.registrationVerificationRoute(
    navigateBack: () -> Unit,
    onRegistrationVerified: () -> Unit
) {
    composable(route = AuthenticationNavigation.RegistrationVerification.route){
        RegistrationVerificationScreen(
            onVerified = onRegistrationVerified,
            navigateBack = navigateBack
        )
    }
}
