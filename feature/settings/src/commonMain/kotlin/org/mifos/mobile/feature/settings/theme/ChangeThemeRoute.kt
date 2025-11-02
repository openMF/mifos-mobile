/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.theme

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import org.mifos.mobile.core.ui.composableWithPushTransitions
import org.mifos.mobile.feature.settings.componenets.SettingsItems

/**
 * Defines the composable destination for the "Theme" selection screen within the navigation graph.
 *
 * This function sets up the route and the content to be displayed (`ChangeThemeScreen`),
 * along with screen transitions.
 *
 * @param navigateBack A lambda function to be invoked when the user initiates a back action
 *   from the [ChangeThemeScreen].
 */
internal fun NavGraphBuilder.themeDestination(
    navigateBack: () -> Unit,
) {
    composableWithPushTransitions<SettingsItems.Theme> {
        ChangeThemeScreen(
            onNavigateBack = navigateBack,
        )
    }
}

/**
 * Navigates to the "Theme" selection screen.
 *
 * This is an extension function on [NavController] that simplifies the process
 * of navigating to the theme destination using a predefined route.
 *
 * @param navOptions Optional [NavOptions] to apply to this navigation operation.
 */
internal fun NavController.navigateToTheme(navOptions: NavOptions? = null) =
    navigate(SettingsItems.Theme, navOptions)
