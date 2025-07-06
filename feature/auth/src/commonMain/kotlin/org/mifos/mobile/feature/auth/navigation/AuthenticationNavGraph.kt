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
import androidx.navigation.compose.navigation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mifos.mobile.feature.auth.login.LoginRoute
import org.mifos.mobile.feature.auth.login.loginDestination
import org.mifos.mobile.feature.auth.login.navigateToLogin
import org.mifos.mobile.feature.auth.registration.navigateToRegistration
import org.mifos.mobile.feature.auth.registration.registrationDestination

@Serializable
@SerialName("auth_graph")
data object AuthGraphRoute

fun NavController.navigateToLoginScreen() {
    navigate(LoginRoute) {
        popUpTo(LoginRoute) { inclusive = true }
    }
}

fun NavGraphBuilder.authenticationNavGraph(
    navController: NavHostController,
    navigateToPasscodeScreen: () -> Unit,
) {
    navigation<AuthGraphRoute>(
        startDestination = LoginRoute,
    ) {
        loginDestination(
            navigateToRegisterScreen = navController::navigateToRegistration,
            navigateToPasscodeScreen = navigateToPasscodeScreen,
        )

        registrationDestination(
            navigateToLogin = navController::navigateToLogin,
            navigateToUploadDocuments = { },
        )
    }
}
