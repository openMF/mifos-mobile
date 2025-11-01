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
 * Defines the composable destination for the "Theme" screen within the navigation graph.
 *
 * @param navigateBack A lambda function to handle the back navigation event.
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
 * Navigates to the Theme selection screen.
 *
 * @param navOptions Optional [NavOptions] to configure the navigation action.
 */
internal fun NavController.navigateToTheme(navOptions: NavOptions? = null) =
    navigate(SettingsItems.Theme, navOptions)
