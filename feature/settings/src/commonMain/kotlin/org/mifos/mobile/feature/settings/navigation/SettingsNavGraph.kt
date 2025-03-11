/*
 * Copyright 2024 Mifos Initiative
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
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.feature.settings.SettingsScreen

fun NavController.navigateToSettings() {
    navigate(SettingsNavigation.SettingsBase.route)
}

fun NavGraphBuilder.settingsNavGraph(
    navigateBack: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    changePassword: () -> Unit,
    changePasscode: (String) -> Unit,
    languageChanged: () -> Unit,
) {
    navigation(
        startDestination = SettingsNavigation.SettingsScreen.route,
        route = SettingsNavigation.SettingsBase.route,
    ) {
        settingsScreenRoute(
            navigateBack = navigateBack,
            navigateToLoginScreen = navigateToLoginScreen,
            changePassword = changePassword,
            changePasscode = changePasscode,
            languageChanged = languageChanged,
        )
    }
}

fun NavGraphBuilder.settingsScreenRoute(
    navigateBack: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    changePassword: () -> Unit,
    changePasscode: (String) -> Unit,
    languageChanged: () -> Unit,
) {
    composable(
        route = SettingsNavigation.SettingsScreen.route,
    ) {
        SettingsScreen(
            navigateBack = navigateBack,
            navigateToLoginScreen = navigateToLoginScreen,
            changePassword = changePassword,
            changePasscode = changePasscode,
            languageChanged = languageChanged,
        )
    }
}
