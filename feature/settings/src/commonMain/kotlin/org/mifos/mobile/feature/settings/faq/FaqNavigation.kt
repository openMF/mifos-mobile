/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.faq

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import org.mifos.mobile.core.ui.composableWithPushTransitions
import org.mifos.mobile.feature.settings.componenets.SettingsItems

/**
 * Defines the composable destination for the "FAQ" (Frequently Asked Questions) screen
 * within the navigation graph.
 *
 * @param onBackClick A lambda function to be invoked when the user initiates a back action.
 * @param contact A lambda function to handle navigation to a contact or help screen.
 */
fun NavGraphBuilder.faqDestination(
    onBackClick: () -> Unit,
    contact: () -> Unit,
) {
    composableWithPushTransitions<SettingsItems.FAQ> {
        FaqScreen(
            onNavigateBack = onBackClick,
            onClickHelp = contact,
        )
    }
}

/**
 * Navigates to the "FAQ" screen. This is an extension function on [NavController]
 * that simplifies the process of navigating to the FAQ destination.
 *
 * @param navOptions Optional [NavOptions] to apply to this navigation operation.
 */
fun NavController.navigateToFaq(navOptions: NavOptions? = null) =
    navigate(SettingsItems.FAQ, navOptions)
