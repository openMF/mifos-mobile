/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:Suppress("MatchingDeclarationName")

package org.mifos.mobile.feature.auth.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithStayTransitions

@Serializable
@SerialName("login")
data object LoginRoute

fun NavController.navigateToLoginScreen(navOptions: NavOptions? = null) {
    this.navigate(route = LoginRoute, navOptions = navOptions)
}

fun NavGraphBuilder.loginDestination(
    navigateToRegisterScreen: () -> Unit,
    navigateToPasscodeScreen: () -> Unit,
    navigateToForgotPasswordScreen: () -> Unit,
) {
    composableWithStayTransitions<LoginRoute> {
        LoginScreen(
            navigateToRegisterScreen = navigateToRegisterScreen,
            navigateToPasscodeScreen = navigateToPasscodeScreen,
            navigateToForgotPasswordScreen = navigateToForgotPasswordScreen,
        )
    }
}
