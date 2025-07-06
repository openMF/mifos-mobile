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

package org.mifos.mobile.feature.auth.uploadId

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithPushTransitions

@Serializable
data object UploadIdRoute

fun NavController.navigateToUploadIdScreen(navOptions: NavOptions? = null) {
    this.navigate(route = UploadIdRoute, navOptions = navOptions)
}

fun NavGraphBuilder.uploadIdDestination(
    navigateToRegisterScreen: () -> Unit,
    navigateToOtpAuthenticationScreen: () -> Unit,
) {
    composableWithPushTransitions<UploadIdRoute> {
        UploadIdScreen(
            navigateToRegisterScreen = navigateToRegisterScreen,
            navigateToOtpAuthenticationScreen = navigateToOtpAuthenticationScreen,
        )
    }
}
