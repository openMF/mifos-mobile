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

internal fun NavController.navigateToHelp(navOptions: NavOptions? = null) =
    navigate(SettingsItems.Help, navOptions)

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
