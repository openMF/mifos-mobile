/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:Suppress("MatchingDeclarationName")

package org.mifos.mobile.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithStayTransitions
import org.mifos.mobile.feature.home.HomeScreen

@Serializable
data object HomeRoute

fun NavController.navigateToHomeScreen(navOptions: NavOptions? = null) =
    navigate(HomeRoute, navOptions)

fun NavGraphBuilder.homeDestination(
//    navigateToDestinationScreen: (route: String) -> Unit,
    navigateToAccountsScreen: (String) -> Unit,
    navigateToNotificationScreen: () -> Unit,
) {
    composableWithStayTransitions<HomeRoute> {
        HomeScreen(
//            navigateToDestinationScreen = navigateToDestinationScreen,
            navigateToAccountsScreen = navigateToAccountsScreen,
            navigateToNotificationScreen = navigateToNotificationScreen,
        )
    }
}
