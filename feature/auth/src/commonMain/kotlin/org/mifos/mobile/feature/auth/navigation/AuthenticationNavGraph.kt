/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:Suppress("MatchingDeclarationName")

package org.mifos.mobile.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mifos.mobile.feature.auth.login.LoginRoute
import org.mifos.mobile.feature.auth.login.loginDestination
import org.mifos.mobile.feature.auth.login.navigateToLoginScreen
import org.mifos.mobile.feature.auth.otpAuthentication.navigateToOtpAuthScreen
import org.mifos.mobile.feature.auth.otpAuthentication.otpAuthenticationDestination
import org.mifos.mobile.feature.auth.recoverPassword.navigateToRecoverPasswordScreen
import org.mifos.mobile.feature.auth.recoverPassword.recoverPasswordDestination
import org.mifos.mobile.feature.auth.registration.navigateToRegisterScreen
import org.mifos.mobile.feature.auth.registration.registrationDestination
import org.mifos.mobile.feature.auth.setNewPassword.navigateToSetPasswordScreen
import org.mifos.mobile.feature.auth.setNewPassword.setPasswordDestination
import org.mifos.mobile.feature.auth.status.navigateToStatusScreen
import org.mifos.mobile.feature.auth.status.statusDestination
import org.mifos.mobile.feature.auth.uploadId.navigateToUploadIdScreen
import org.mifos.mobile.feature.auth.uploadId.uploadIdDestination

@Serializable
@SerialName("auth_graph")
data object AuthGraphRoute

fun NavController.navigateToAuthGraph(navOptions: NavOptions? = null) {
    this.navigate(AuthGraphRoute, navOptions)
}

fun NavGraphBuilder.authenticationNavGraph(
    navController: NavHostController,
    navigateToPasscodeScreen: () -> Unit,
) {
    navigation<AuthGraphRoute>(
        startDestination = LoginRoute,
    ) {
        loginDestination(
            navigateToRegisterScreen = navController::navigateToRegisterScreen,
            navigateToPasscodeScreen = navigateToPasscodeScreen,
            navigateToForgotPasswordScreen = navController::navigateToRecoverPasswordScreen,
        )

        registrationDestination(
            navigateToLoginScreen = navController::navigateToLoginScreen,
            navigateToUploadIdScreen = navController::navigateToUploadIdScreen,
        )

        uploadIdDestination(
            navigateToRegisterScreen = navController::navigateToRegisterScreen,
            navigateToOtpAuthenticationScreen = navController::navigateToOtpAuthScreen,
        )

        otpAuthenticationDestination(
            navigateBack = navController::popBackStack,
            navigateToStatusScreen = navController::navigateToStatusScreen,
            navigateToSetPasswordScreen = navController::navigateToSetPasswordScreen,
        )

        statusDestination(
            navigateToDestination = {
                if (it == "login") {
                    navController.navigateToLoginScreen()
                } else {
                    navController.navigate(it)
                }
            },
        )

        recoverPasswordDestination(
            navigateToLoginScreen = navController::navigateToLoginScreen,
            navigateToOtpAuthenticationScreen = navController::navigateToOtpAuthScreen,
        )

        setPasswordDestination(
            navigateToStatusScreen = navController::navigateToStatusScreen,
            navigateToLoginScreen = navController::navigateToLoginScreen,
        )
    }
}
