/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.feature.settings.about.aboutDestination
import org.mifos.mobile.feature.settings.appInfo.appInfoDestination
import org.mifos.mobile.feature.settings.componenets.SettingsItems
import org.mifos.mobile.feature.settings.help.helpDestination
import org.mifos.mobile.feature.settings.settings.SettingsRoute
import org.mifos.mobile.feature.settings.settings.settingsDestination

@Serializable
data object SettingsNavGraphRoute

fun NavController.navigateToSettingsGraph(navOptions: NavOptions? = null) =
    navigate(SettingsNavGraphRoute, navOptions)

fun NavGraphBuilder.settingsGraph(
    navController: NavController,
) {
    navigation<SettingsNavGraphRoute>(
        startDestination = SettingsRoute,
    ) {
        settingsDestination(
            navigateBack = navController::popBackStack,
            navigateToScreen = navController::navigateToScreen,
        )
        helpDestination(
            onBackClick = navController::popBackStack,
            navigateToFAQ = {},
        )
        aboutDestination(
            onBackClick = navController::popBackStack,
        )
        appInfoDestination(
            onBackClick = navController::popBackStack,
            navigateToPrivacyPolicy = {},
            navigateToTermsAndConditions = {},
        )
    }
}

internal fun NavController.navigateToScreen(
    route: SettingsItems,
    navOptions: NavOptions? = null,
) = navigate(route, navOptions)
