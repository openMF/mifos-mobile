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

package org.mifos.mobile.feature.auth.registration

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithStayTransitions

@Serializable
data object RegistrationRoute

fun NavController.navigateToRegisterScreen(navOptions: NavOptions? = null) {
    this.navigate(route = RegistrationRoute, navOptions = navOptions)
}

fun NavGraphBuilder.registrationDestination(
    navigateToLoginScreen: () -> Unit,
    navigateToUploadIdScreen: () -> Unit,
) {
    composableWithStayTransitions<RegistrationRoute> {
        RegistrationScreen(
            navigateToLoginScreen = navigateToLoginScreen,
            navigateToUploadIdScreen = navigateToUploadIdScreen,
        )
    }
}
