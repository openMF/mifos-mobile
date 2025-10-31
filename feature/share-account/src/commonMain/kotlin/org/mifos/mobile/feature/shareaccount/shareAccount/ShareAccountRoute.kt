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

package org.mifos.mobile.feature.shareaccount.shareAccount

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * A serializable object representing the route to the Share Account screen.
 */
@Serializable
data object ShareAccountRoute

/**
 * Navigates to the Share Account screen.
 *
 * @param navOptions Optional navigation options.
 */
fun NavController.navigateToShareAccountScreen(navOptions: NavOptions? = null) =
    navigate(ShareAccountRoute, navOptions)

/**
 * Defines the destination for the Share Account screen in the navigation graph.
 *
 * @param navigateBack A function to navigate back to the previous screen.
 */
fun NavGraphBuilder.shareAccountDestination(
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<ShareAccountRoute> {
        ShareAccountScreen(
            navigateBack = navigateBack,
        )
    }
}
