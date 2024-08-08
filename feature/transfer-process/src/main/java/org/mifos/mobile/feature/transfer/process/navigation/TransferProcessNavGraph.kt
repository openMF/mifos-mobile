package org.mifos.mobile.feature.transfer.process.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.feature.transfer.process.TransferProcessScreen

// Navigation Setup
fun NavController.navigateToTransferProcessScreen() {
    navigate(TransferProcessNavigation.TransferProcessScreen.route)
}

fun NavGraphBuilder.transferProcessNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = TransferProcessNavigation.TransferProcessScreen.route,
        route = TransferProcessNavigation.TransferProcessBase.route,
    ) {
        transferProcessScreenRoute(
            navigateBack = navController::popBackStack,
        )
    }
}

fun NavGraphBuilder.transferProcessScreenRoute(
    navigateBack: () -> Unit,
) {
    composable(
        route = TransferProcessNavigation.TransferProcessScreen.route,
    ) {
        TransferProcessScreen(
            navigateBack = navigateBack,
        )
    }
}