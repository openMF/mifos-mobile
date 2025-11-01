/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.password

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import org.mifos.mobile.core.ui.composableWithPushTransitions
import org.mifos.mobile.feature.settings.componenets.SettingsItems

/**
 * Navigates to the Change Password screen.
 *
 * @param navOptions Optional [NavOptions] to configure the navigation action.
 */
fun NavController.navigateToUpdatePassword(navOptions: NavOptions? = null) =
    navigate(SettingsItems.Password, navOptions)

/**
 * Defines the composable destination for the "Change Password" screen within the navigation graph.
 *
 * @param onBackClick A lambda function to handle the back navigation event.
 */
internal fun NavGraphBuilder.changePasswordDestination(
    onBackClick: () -> Unit,
) {
    composableWithPushTransitions<SettingsItems.Password> {
        ChangePasswordScreen(
            navigateBack = onBackClick,
        )
    }
}
