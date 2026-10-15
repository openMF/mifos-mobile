/*
 * Copyright 2026 Mifos Initiative
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

/**
 * A type-safe, serializable data class representing the route for the
 * "Make Transfer" screen. This serves as the entry point for the transfer
 * creation flow, containing all necessary parameters to initialize the screen.
 *
 * @param accountId The ID of the account from which the transfer will be made. Default is -1L.
 * @param accountNo The account number from which the transfer will be made. Optional.
 * @param amount The pre-filled transfer amount. Optional.
 * @param outstandingBalance The outstanding balance for loan repayments. Optional.
 * @param transferType The type of transfer (e.g., "LOAN_REPAYMENT"). Optional.
 * @param transferTarget The target of the transfer (e.g., "SELF", "THIRD_PARTY"). Optional.
 * @param transferSuccessDestination The destination screen to navigate to after successful transfer. Optional.
 */
@Serializable
data class MakeTransferRoute(
    val accountId: Long = -1L,
    val accountNo: String? = null,
    val amount: String? = null,
    val outstandingBalance: String? = null,
    val transferType: String? = null,
    val transferTarget: String? = null,
    val transferSuccessDestination: String? = null,
)

/**
 * Navigates to the "Make Transfer" screen.
 *
 * This is an extension function on [NavController] that simplifies navigating
 * to the make transfer screen by extracting necessary information from the [AccountDetails].
 *
 * @param transferPayload The account details containing transfer information.
 * @param navOptions Optional [NavOptions] to apply to this navigation operation.
 */
fun NavController.navigateToMakeTransferScreen(transferPayload: AccountDetails, navOptions: NavOptions? = null) =
    navigate(
        MakeTransferRoute(
            accountId = transferPayload.accountId,
            accountNo = transferPayload.accountNo,
            outstandingBalance = transferPayload.outstandingBalance.toString(),
            transferType = transferPayload.transferType,
            transferTarget = transferPayload.transferTarget.name,
            transferSuccessDestination = transferPayload.transferSuccessDestination,
        ),
        navOptions,
    )

/**
 * Defines the composable destination for the "Make Transfer" screen
 * within the navigation graph.
 *
 * This function sets up the route, the screen content ([MakeTransferScreen]),
 * and wires up the navigation callbacks for actions initiated from this screen.
 *
 * @param navigateBack A lambda function to handle the back navigation event.
 * @param navigateToTransferScreen A lambda to navigate to the transfer review screen,
 *   passing the transfer payload, transfer type, and destination.
 */
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
