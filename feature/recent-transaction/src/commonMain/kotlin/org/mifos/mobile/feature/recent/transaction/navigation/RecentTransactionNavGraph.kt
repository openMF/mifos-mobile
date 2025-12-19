/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.recent.transaction.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.feature.recent.transaction.screen.RecentTransactionScreen

/**
 * Navigates to the recent transactions feature graph.
 */
fun NavController.navigateToRecentTransactionScreen() {
    navigate(RecentTransactionNavigation.RecentTransactionBase.route)
}

/**
 * Defines the nested navigation graph for the recent transactions feature.
 * This graph includes all screens related to recent transactions.
 *
 * @param navController The [NavController] used to handle navigation events.
 */
fun NavGraphBuilder.recentTransactionNavGraph(
    navController: NavController,
    navigateToDetails: (String, String, Long) -> Unit,
) {
    navigation(
        startDestination = RecentTransactionNavigation.RecentTransactionScreen.route,
        route = RecentTransactionNavigation.RecentTransactionBase.route,
    ) {
        recentTransactionScreenRoute(
            navigateBack = navController::popBackStack,
            navigateToDetails = navigateToDetails,
        )
    }
}

/**
 * Defines the composable route for the main [RecentTransactionScreen].
 *
 * @param navigateBack Callback to navigate back to the previous screen.
 */
fun NavGraphBuilder.recentTransactionScreenRoute(
    navigateBack: () -> Unit,
    navigateToDetails: (String, String, Long) -> Unit,
) {
    composable(
        route = RecentTransactionNavigation.RecentTransactionScreen.route,
    ) {
        RecentTransactionScreen(
            navigateBack = navigateBack,
            navigateToDetails = navigateToDetails,
        )
    }
}
