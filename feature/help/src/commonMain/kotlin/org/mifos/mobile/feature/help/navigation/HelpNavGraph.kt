/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.help.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.feature.help.HelpScreen

fun NavController.navigateToHelpScreen() {
    navigate(HelpNavigation.HelpScreen.route)
}

fun NavGraphBuilder.helpNavGraph(
    findLocations: () -> Unit,
    navigateBack: () -> Unit,
    callHelpline: () -> Unit,
    mailHelpline: () -> Unit,
) {
    navigation(
        startDestination = HelpNavigation.HelpScreen.route,
        route = HelpNavigation.HelpBase.route,
    ) {
        helpScreenRoute(
            findLocations = findLocations,
            navigateBack = navigateBack,
            callHelpline = callHelpline,
            mailHelpline = mailHelpline,
        )
    }
}

fun NavGraphBuilder.helpScreenRoute(
    findLocations: () -> Unit,
    navigateBack: () -> Unit,
    callHelpline: () -> Unit,
    mailHelpline: () -> Unit,
) {
    composable(
        route = HelpNavigation.HelpScreen.route,
    ) {
        HelpScreen(
            callNow = callHelpline,
            leaveEmail = mailHelpline,
            findLocations = findLocations,
            navigateBack = navigateBack,
        )
    }
}
