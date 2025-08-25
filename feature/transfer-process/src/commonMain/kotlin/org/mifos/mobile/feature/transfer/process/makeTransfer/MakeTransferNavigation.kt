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

package org.mifos.mobile.feature.transfer.process.makeTransfer

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.entity.AccountDetails
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.composableWithSlideTransitions

@Serializable
data class MakeTransferRoute(
    val accountId: Long = -1L,
    val accountNo: String? = null,
    val amount: Double? = null,
    val outstandingBalance: Double? = null,
    val transferType: String? = null,
    val transferTarget: String? = null,
    val transferSuccessDestination: String? = null,
)

fun NavController.navigateToMakeTransferScreen(transferPayload: AccountDetails, navOptions: NavOptions? = null) =
    navigate(
        MakeTransferRoute(
            accountId = transferPayload.accountId,
            accountNo = transferPayload.accountNo,
            outstandingBalance = transferPayload.outstandingBalance,
            transferType = transferPayload.transferType,
            transferTarget = transferPayload.transferTarget.name,
            transferSuccessDestination = transferPayload.transferSuccessDestination,
        ),
        navOptions,
    )

fun NavGraphBuilder.makeTransferDestination(
    navigateBack: () -> Unit,
    navigateToTransferScreen: (ReviewTransferPayload, TransferType, String) -> Unit,
) {
    composableWithSlideTransitions<MakeTransferRoute> {
        MakeTransferScreen(
            navigateBack = navigateBack,
            navigateToTransferScreen = navigateToTransferScreen,
        )
    }
}
