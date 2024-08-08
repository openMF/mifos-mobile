package org.mifos.mobile.feature.recent_transaction.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.feature.recent_transaction.screens.RecentTransactionScreen

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