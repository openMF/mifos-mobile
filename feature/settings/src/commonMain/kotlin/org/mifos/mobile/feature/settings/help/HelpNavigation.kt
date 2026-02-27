/*
 * Copyright 2026 Mifos Initiative
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
 * Navigates to the "Help" screen. This is an extension function on [NavController]
 * that simplifies the process of navigating to the help destination.
 *
 * @param navOptions Optional [NavOptions] to apply to this navigation operation.
 */
fun NavController.navigateToHelp(navOptions: NavOptions? = null) =
    navigate(SettingsItems.Help, navOptions)

/**
 * Defines the composable destination for the "Help" screen within the navigation graph.
 * This sets up the route and the content to be displayed, along with screen transitions.
 *
 * @param onBackClick A lambda function to be invoked when the user initiates a back action.
 * @param navigateToFAQ A lambda function to navigate to the "FAQ" screen.
 */
fun NavGraphBuilder.helpDestination(
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
