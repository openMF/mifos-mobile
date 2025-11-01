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
 * A serializable object representing the route for the main Settings screen.
 * This is used by the navigation component to identify this destination.
 */
@Serializable
data object SettingsRoute

/**
 * Navigates to the main Settings screen.
 *
 * @param navOptions Optional [NavOptions] to configure the navigation action, such as
 *   transitions or launch modes.
 */
fun NavController.navigateToSettingsRoute(navOptions: NavOptions? = null) {
    this.navigate(SettingsRoute, navOptions)
}

/**
 * Defines the composable destination for the main "Settings" screen within the navigation graph.
 *
 * @param navigateBack A lambda function to handle the back navigation event.
 * @param navigateToScreen A lambda function to handle navigation to other specific setting screens.
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
