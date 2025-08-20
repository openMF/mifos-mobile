/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.share.application.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.feature.share.application.fillApplication.navigateToShareFillApplicationScreen
import org.mifos.mobile.feature.share.application.fillApplication.shareFillApplicationDestination
import org.mifos.mobile.feature.share.application.shareApplication.ShareApplyRoute
import org.mifos.mobile.feature.share.application.shareApplication.shareApplyDestination

@Serializable
data object ShareApplicationNavGraph

fun NavController.navigateToShareApplicationGraph(navOptions: NavOptions? = null) {
    this.navigate(ShareApplicationNavGraph, navOptions)
}

fun NavGraphBuilder.shareApplicationNavGraph(
    navController: NavController,
    navigateToAuthenticateScreen: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
) {
    navigation<ShareApplicationNavGraph>(
        startDestination = ShareApplyRoute,
    ) {
        shareApplyDestination(
            navigateBack = navController::popBackStack,
            navigateToFillDetailsScreen = navController::navigateToShareFillApplicationScreen,
        )

        shareFillApplicationDestination(
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
            navigateToStatusScreen = navigateToStatusScreen,
            navigateBack = navController::popBackStack,
        )
    }
}
