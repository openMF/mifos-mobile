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
 * Navigates to the "Change Password" screen.
 *
 * This is an extension function on [NavController] that simplifies the process
 * of navigating to the change password destination using a predefined route.
 *
 * @param navOptions Optional [NavOptions] to apply to this navigation operation.
 */
fun NavController.navigateToUpdatePassword(navOptions: NavOptions? = null) =
    navigate(SettingsItems.Password, navOptions)

/**
 * Defines the composable destination for the "Change Password" screen within the navigation graph.
 *
 * This function sets up the route and the content to be displayed, along with screen
 * transitions.
 *
 * @param onBackClick A lambda function to be invoked when the user initiates a back action
 *   from the [ChangePasswordScreen].
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
