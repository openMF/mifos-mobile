/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transaction.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.feature.transaction.screens.RecentTransactionScreen

fun NavController.navigateToRecentTransaction() {
    navigate(RecentTransactionNavigation.RecentTransactionBase.route)
}

fun NavGraphBuilder.recentTransactionNavGraph(
    navigateBack: () -> Unit,
) {
    navigation(
        startDestination = RecentTransactionNavigation.RecentTransactionScreen.route,
        route = RecentTransactionNavigation.RecentTransactionBase.route,
    ) {
        settingsScreenRoute(
            navigateBack = navigateBack,
        )
    }
}

fun NavGraphBuilder.settingsScreenRoute(
    navigateBack: () -> Unit,
) {
    composable(
        route = RecentTransactionNavigation.RecentTransactionScreen.route,
    ) {
        RecentTransactionScreen(
            navigateBack = navigateBack,
        )
    }
}
