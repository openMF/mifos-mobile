package org.mifos.mobile.feature.third.party.transfer.third_party_transfer.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.feature.third.party.transfer.third_party_transfer.ThirdPartyTransferPayload
import org.mifos.mobile.feature.third.party.transfer.third_party_transfer.ThirdPartyTransferScreen

fun NavController.navigateToThirdPartyTransfer() {
    navigate(ThirdPartyTransferNavigation.ThirdPartyTransferBase.route)
}

fun NavGraphBuilder.thirdPartyTransferNavGraph(
    navController: NavController,
    addBeneficiary: () -> Unit,
    reviewTransfer: (ReviewTransferPayload, TransferType) -> Unit
) {
    navigation(
        startDestination = ThirdPartyTransferNavigation.ThirdPartyTransferScreen.route,
        route = ThirdPartyTransferNavigation.ThirdPartyTransferBase.route,
    ) {
        thirdPartyTransferRoute(
            navigateBack = navController::popBackStack,
            addBeneficiary = addBeneficiary,
            reviewTransfer = reviewTransfer
        )
    }
}

fun NavGraphBuilder.thirdPartyTransferRoute(
    navigateBack: () -> Unit,
    addBeneficiary: () -> Unit,
    reviewTransfer: (ReviewTransferPayload, TransferType) -> Unit
) {
    composable(
        route = ThirdPartyTransferNavigation.ThirdPartyTransferScreen.route,
    ) {
        ThirdPartyTransferScreen(
            navigateBack = navigateBack,
            addBeneficiary = addBeneficiary ,
            reviewTransfer = reviewTransfer
        )
    }
}