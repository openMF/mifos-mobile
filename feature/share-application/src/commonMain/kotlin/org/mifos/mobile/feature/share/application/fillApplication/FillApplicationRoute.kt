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

package org.mifos.mobile.feature.share.application.fillApplication

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * A serializable data class representing the route to the Share Fill Application screen.
 *
 * @property shareProductId The ID of the share product to apply for.
 */
@Serializable
data class ShareFillApplicationRoute(
    val shareProductId: Long,
)

/**
 * Navigates to the Share Fill Application screen.
 *
 * @param shareProductId The ID of the share product.
 * @param navOptions Optional navigation options.
 */
fun NavController.navigateToShareFillApplicationScreen(
    shareProductId: Long,
    navOptions: NavOptions? = null,
) {
    this.navigate(ShareFillApplicationRoute(shareProductId), navOptions)
}

/**
 * Defines the destination for the Share Fill Application screen in the navigation graph.
 *
 * @param navigateToAuthenticateScreen A function to navigate to the authentication screen.
 * @param navigateToStatusScreen A function to navigate to the status screen.
 * @param navigateBack A function to navigate back to the previous screen.
 */
fun NavGraphBuilder.shareFillApplicationDestination(
    navigateToAuthenticateScreen: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<ShareFillApplicationRoute> {
        ShareFillApplicationScreen(
            navigateBack = navigateBack,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
            navigateToStatusScreen = navigateToStatusScreen,
        )
    }
}
