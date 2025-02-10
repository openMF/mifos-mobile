/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.feature.auth.login.LoginScreen
import org.mifos.mobile.feature.auth.registration.RegistrationScreen
import org.mifos.mobile.feature.auth.registration.RegistrationVerificationScreen

fun NavController.navigateToLoginScreen() {
    navigate(AuthenticationNavigation.Login.route) {
        popUpTo(AuthenticationNavigation.Login.route) { inclusive = true }
    }
}

fun NavGraphBuilder.authenticationNavGraph(
    navController: NavHostController,
    route: String,
    navigateToPasscodeScreen: () -> Unit,
) {
    navigation(
        route = route,
        startDestination = AuthenticationNavigation.Login.route,
    ) {
        loginRoute(
            navigateToRegisterScreen = {
                navController.navigate(AuthenticationNavigation.Registration.route)
            },
            navigateToPasscodeScreen = navigateToPasscodeScreen,
        )

        registrationRoute(
            navigateBack = navController::popBackStack,
            onRegistered = {
                navController.navigate(AuthenticationNavigation.RegistrationVerification.route)
            },
        )

        registrationVerificationRoute(
            navigateBack = navController::popBackStack,
            onRegistrationVerified = navController::navigateToLoginScreen,
        )
    }
}

private fun NavGraphBuilder.loginRoute(
    navigateToRegisterScreen: () -> Unit,
    navigateToPasscodeScreen: () -> Unit,
) {
    composable(route = AuthenticationNavigation.Login.route) {
        LoginScreen(
            navigateToRegisterScreen = navigateToRegisterScreen,
            navigateToPasscodeScreen = navigateToPasscodeScreen,
        )
    }
}

private fun NavGraphBuilder.registrationRoute(
    navigateBack: () -> Unit,
    onRegistered: () -> Unit,
) {
    composable(route = AuthenticationNavigation.Registration.route) {
        RegistrationScreen(
            navigateToVerification = onRegistered,
            navigateBack = navigateBack,
        )
    }
}

private fun NavGraphBuilder.registrationVerificationRoute(
    navigateBack: () -> Unit,
    onRegistrationVerified: () -> Unit,
) {
    composable(route = AuthenticationNavigation.RegistrationVerification.route) {
        RegistrationVerificationScreen(
            navigateToLogin = onRegistrationVerified,
            navigateToRegister = navigateBack,
        )
    }
}
