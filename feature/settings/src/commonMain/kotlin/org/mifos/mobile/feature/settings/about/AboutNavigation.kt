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
 * Defines the "About Us" screen destination in the navigation graph.
 *
 * @param onBackClick Lambda to handle back navigation.
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
 * @param navOptions Optional navigation options.
 */
internal fun NavController.navigateToAbout(navOptions: NavOptions? = null) =
    navigate(SettingsItems.AboutUs, navOptions)
