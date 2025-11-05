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
import org.mifos.mobile.feature.settings.faq.faqDestination
import org.mifos.mobile.feature.settings.help.helpDestination
import org.mifos.mobile.feature.settings.language.languageDestination
import org.mifos.mobile.feature.settings.passcode.updatePasscodeDestination
import org.mifos.mobile.feature.settings.password.changePasswordDestination
import org.mifos.mobile.feature.settings.settings.SettingsRoute
import org.mifos.mobile.feature.settings.settings.settingsDestination
import org.mifos.mobile.feature.settings.theme.themeDestination

/**
 * A serializable object representing the route for the nested settings navigation graph.
 * This serves as the entry point for all settings-related screens.
 */
@Serializable
data object SettingsNavGraphRoute

/**
 * Navigates to the settings navigation graph. This is a convenience extension function
 * on [NavController] to encapsulate the navigation logic to the settings feature.
 *
 * @param navOptions Optional [NavOptions] to apply to this navigation operation.
 */
fun NavController.navigateToSettingsGraph(navOptions: NavOptions? = null) =
    navigate(SettingsNavGraphRoute, navOptions)

/**
 * Builds the nested navigation graph for the settings feature. This function defines
 * all the destinations within the settings module and their corresponding navigation actions.
 *
 * @param navController The [NavController] used for handling navigation events within the graph.
 */
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
            navigateToFAQ = { navController.navigateToScreen(SettingsItems.FAQ) },
        )
        aboutDestination(
            onBackClick = navController::popBackStack,
        )
        appInfoDestination(
            onBackClick = navController::popBackStack,
            navigateToPrivacyPolicy = {},
            navigateToTermsAndConditions = {},
        )
        updatePasscodeDestination(
            navigateBack = navController::popBackStack,
        )
        languageDestination(
            navigateBack = navController::popBackStack,
        )
        faqDestination(
            onBackClick = navController::popBackStack,
            contact = { navController.navigateToScreen(SettingsItems.Help) },
        )
        changePasswordDestination(
            onBackClick = navController::popBackStack,
        )
        themeDestination(
            navigateBack = navController::popBackStack,
        )
    }
}

/**
 * A generic internal helper function to navigate to any screen within the settings graph
 * using its [SettingsItems] route.
 *
 * @param route The [SettingsItems] destination to navigate to.
 * @param navOptions Optional [NavOptions] to apply to this navigation operation.
 */
internal fun NavController.navigateToScreen(
    route: SettingsItems,
    navOptions: NavOptions? = null,
) = navigate(route, navOptions)
