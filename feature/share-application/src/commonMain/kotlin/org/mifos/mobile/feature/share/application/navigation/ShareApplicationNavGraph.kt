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

/**
 * A serializable object representing the navigation graph for the Share Application feature.
 */
@Serializable
data object ShareApplicationNavGraph

/**
 * Navigates to the Share Application navigation graph.
 *
 * @param navOptions Optional navigation options.
 */
fun NavController.navigateToShareApplicationGraph(navOptions: NavOptions? = null) {
    this.navigate(ShareApplicationNavGraph, navOptions)
}

/**
 * Defines the navigation graph for the Share Application feature.
 *
 * @param navController The [NavController] for the graph.
 * @param navigateToAuthenticateScreen A function to navigate to the authentication screen.
 * @param navigateToStatusScreen A function to navigate to the status screen.
 */
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
