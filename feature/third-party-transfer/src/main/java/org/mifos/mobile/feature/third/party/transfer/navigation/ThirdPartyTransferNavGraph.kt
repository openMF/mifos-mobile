/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.third.party.transfer.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.feature.third.party.transfer.ThirdPartyTransferScreen

fun NavController.navigateToThirdPartyTransfer() {
    navigate(ThirdPartyTransferNavigation.ThirdPartyTransferBase.route)
}

fun NavGraphBuilder.thirdPartyTransferNavGraph(
    navigateBack: () -> Unit,
    addBeneficiary: () -> Unit,
    reviewTransfer: (ReviewTransferPayload, TransferType) -> Unit,
) {
    navigation(
        startDestination = ThirdPartyTransferNavigation.ThirdPartyTransferScreen.route,
        route = ThirdPartyTransferNavigation.ThirdPartyTransferBase.route,
    ) {
        thirdPartyTransferRoute(
            navigateBack = navigateBack,
            addBeneficiary = addBeneficiary,
            reviewTransfer = reviewTransfer,
        )
    }
}

fun NavGraphBuilder.thirdPartyTransferRoute(
    navigateBack: () -> Unit,
    addBeneficiary: () -> Unit,
    reviewTransfer: (ReviewTransferPayload, TransferType) -> Unit,
) {
    composable(
        route = ThirdPartyTransferNavigation.ThirdPartyTransferScreen.route,
    ) {
        ThirdPartyTransferScreen(
            navigateBack = navigateBack,
            addBeneficiary = addBeneficiary,
            reviewTransfer = reviewTransfer,
        )
    }
}
