/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.share.application.shareApplication

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * A serializable object representing the route to the Share Apply screen.
 */
@Serializable
data object ShareApplyRoute

/**
 * Navigates to the Share Apply screen.
 *
 * @param navOptions Optional navigation options.
 */
fun NavController.navigateToShareApplyScreen(
    navOptions: NavOptions? = null,
) =
    navigate(ShareApplyRoute, navOptions)

/**
 * Defines the destination for the Share Apply screen in the navigation graph.
 *
 * @param navigateToFillDetailsScreen A function to navigate to the fill details screen.
 * @param navigateBack A function to navigate back to the previous screen.
 */
fun NavGraphBuilder.shareApplyDestination(
    navigateToFillDetailsScreen: (Long) -> Unit,
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<ShareApplyRoute> {
        ShareApplyScreen(
            navigateBack = navigateBack,
            navigateToFillDetailsScreen = navigateToFillDetailsScreen,
        )
    }
}
