/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:Suppress("MatchingDeclarationName")

package cmp.navigation.authenticatednavbar

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithStayTransitions

@Serializable
data object AuthenticatedNavbarRoute

internal fun NavController.navigateToAuthenticatedNavBar(navOptions: NavOptions? = null) {
    navigate(route = AuthenticatedNavbarRoute, navOptions = navOptions)
}

internal fun NavGraphBuilder.authenticatedNavbarGraph(
    navigateToNotificationScreen: () -> Unit,
    navigateToSavingsScreen: () -> Unit,
) {
    composableWithStayTransitions<AuthenticatedNavbarRoute> {
        AuthenticatedNavbarNavigationScreen(
            navigateToNotificationScreen = navigateToNotificationScreen,
            navigateToSavingsScreen = navigateToSavingsScreen,
        )
    }
}
