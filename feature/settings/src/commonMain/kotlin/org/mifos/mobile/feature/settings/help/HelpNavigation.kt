/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.help

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import org.mifos.mobile.core.ui.composableWithPushTransitions
import org.mifos.mobile.feature.settings.componenets.SettingsItems

/**
 * Navigates to the Help screen.
 *
 * @param navOptions Optional [NavOptions] to configure the navigation action.
 */
internal fun NavController.navigateToHelp(navOptions: NavOptions? = null) =
    navigate(SettingsItems.Help, navOptions)

/**
 * Defines the composable destination for the "Help" screen within the navigation graph.
 *
 * @param onBackClick A lambda function to handle the back button click.
 * @param navigateToFAQ A lambda function to navigate to the FAQ screen.
 */
internal fun NavGraphBuilder.helpDestination(
    onBackClick: () -> Unit,
    navigateToFAQ: () -> Unit,
) {
    composableWithPushTransitions<SettingsItems.Help> {
        HelpScreen(
            onBackClick = onBackClick,
            navigateToFAQ = navigateToFAQ,
        )
    }
}
