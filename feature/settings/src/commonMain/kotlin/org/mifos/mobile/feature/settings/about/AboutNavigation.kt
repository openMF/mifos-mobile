/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.about

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import org.mifos.mobile.core.ui.composableWithPushTransitions
import org.mifos.mobile.feature.settings.componenets.SettingsItems
/**
 * Defines the composable destination for the "About Us" screen within the navigation graph.
 * This sets up the route and the content to be displayed when navigating to the about screen.
 *
 * @param onBackClick A lambda function to be invoked when the back button is clicked,
 * typically to navigate back to the previous screen.
 */
internal fun NavGraphBuilder.aboutDestination(
    onBackClick: () -> Unit,
) {
    composableWithPushTransitions<SettingsItems.AboutUs> {
        AboutScreen(
            onBackClick = onBackClick,
        )
    }
}

/**
 * Navigates to the "About Us" screen.
 *
 * @param navOptions Optional [NavOptions] to configure the navigation action, such as
 * transitions or launch modes.
 */

internal fun NavController.navigateToAbout(navOptions: NavOptions? = null) =
    navigate(SettingsItems.AboutUs, navOptions)
