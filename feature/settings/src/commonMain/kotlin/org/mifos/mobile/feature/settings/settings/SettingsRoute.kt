/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions
import org.mifos.mobile.feature.settings.componenets.SettingsItems

/**
 * A type-safe, serializable route for the main Settings screen.
 * This object serves as the unique identifier for the settings destination
 * in the navigation graph.
 */
@Serializable
data object SettingsRoute

/**
 * Navigates to the main Settings screen.
 *
 * This is an extension function on [NavController] that simplifies the process
 * of navigating to the settings destination.
 *
 * @param navOptions Optional [NavOptions] to apply to this navigation operation.
 */
fun NavController.navigateToSettingsRoute(navOptions: NavOptions? = null) {
    this.navigate(SettingsRoute, navOptions)
}

/**
 * Defines the composable destination for the main "Settings" screen within the navigation graph.
 *
 * This function sets up the route and the content to be displayed, along with screen
 * transitions. It provides callbacks for navigating back and to other specific
 * settings screens.
 *
 * @param navigateBack A lambda function to be invoked when the user initiates a back action.
 * @param navigateToScreen A lambda function to handle navigation to a specific
 *   [SettingsItems] destination.
 */
fun NavGraphBuilder.settingsDestination(
    navigateBack: () -> Unit,
    navigateToScreen: (SettingsItems) -> Unit,
) {
    composableWithSlideTransitions<SettingsRoute> {
        SettingsScreen(
            navigateBack = navigateBack,
            navigateToScreen = navigateToScreen,
        )
    }
}
