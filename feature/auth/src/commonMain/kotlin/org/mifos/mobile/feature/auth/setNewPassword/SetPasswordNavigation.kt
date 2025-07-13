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

package org.mifos.mobile.feature.auth.setNewPassword

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions
import org.mifos.mobile.feature.auth.otpAuthentication.EventType

@Serializable
@SerialName("set_password")
data object SetPasswordRoute

fun NavController.navigateToSetPasswordScreen(navOptions: NavOptions? = null) {
    this.navigate(SetPasswordRoute, navOptions)
}

fun NavGraphBuilder.setPasswordDestination(
    navigateToStatusScreen: (EventType, String, String, String, String) -> Unit,
    navigateToLoginScreen: () -> Unit,
) {
    composableWithSlideTransitions<SetPasswordRoute> {
        SetPasswordScreen(
            navigateToStatusScreen = navigateToStatusScreen,
            navigateToLoginScreen = navigateToLoginScreen,
        )
    }
}
