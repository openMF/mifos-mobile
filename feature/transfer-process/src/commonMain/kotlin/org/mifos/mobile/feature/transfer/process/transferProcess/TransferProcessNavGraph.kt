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

package org.mifos.mobile.feature.transfer.process.transferProcess

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * A type-safe, serializable data class representing the route for the
 * "Transfer Process" screen. This serves as the entry point for the final
 * transfer processing flow, containing all necessary parameters to initialize the screen.
 *
 * @param fromOfficeId The office ID of the source account. Optional.
 * @param fromClientId The client ID of the source account. Optional.
 * @param fromAccountType The account type of the source account. Optional.
 * @param fromAccountId The account ID/number of the source account. Optional.
 * @param fromClientName The client name of the source account. Optional.
 * @param toOfficeId The office ID of the destination account. Optional.
 * @param toClientId The client ID of the destination account. Optional.
 * @param toAccountType The account type of the destination account. Optional.
 * @param toAccountId The account ID/number of the destination account. Optional.
 * @param toClientName The client name of the destination account. Optional.
 * @param transferAmount The amount to be transferred. Optional.
 * @param transferDescription The description or remarks for the transfer. Optional.
 * @param transferType The type of transfer (e.g., "SELF", "THIRD_PARTY"). Default is TransferType.SELF.name.
 * @param transferSuccessDestination Destination screen after successful transfer. Default is empty string.
 */
@Serializable
data class TransferProcessRoute(
    val fromOfficeId: Int? = null,
    val fromClientId: Long? = null,
    val fromAccountType: Int? = null,
    val fromAccountId: String? = null,
    val fromClientName: String? = null,
    val toOfficeId: Int? = null,
    val toClientId: Long? = null,
    val toAccountType: Int? = null,
    val toAccountId: String? = null,
    val toClientName: String? = null,
    val transferAmount: String? = null,
    val transferDescription: String? = null,
    val transferType: String = TransferType.SELF.name,
    val transferSuccessDestination: String = "",
)

/**
 * Defines the composable destination for the "Transfer Process" screen
 * within the navigation graph.
 *
 * This function sets up the route, the screen content ([TransferProcessScreen]),
 * and wires up the navigation callbacks for actions initiated from this screen.
 * The screen handles the final transfer processing after user authentication.
 *
 * @param navigateBack A lambda function to handle the back navigation event.
 * @param navigateToAuthenticateScreen A lambda to navigate to the authentication screen
 *   before proceeding with the transfer.
 * @param navigateToStatusScreen A lambda to navigate to the status screen after transfer
 *   completion with event details.
 */
fun NavGraphBuilder.transferProcessDestination(
    navigateBack: () -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
) {
    composableWithSlideTransitions<TransferProcessRoute> {
        TransferProcessScreen(
            navigateBack = navigateBack,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
            navigateToStatusScreen = navigateToStatusScreen,
        )
    }
}

/**
 * Navigates to the "Transfer Process" screen.
 *
 * This is an extension function on [NavController] that simplifies navigating
 * to the transfer process screen by extracting necessary information from the [ReviewTransferPayload].
 *
 * @param transferPayload The transfer payload containing all transfer details.
 * @param transferType The type of transfer being performed.
 * @param transferSuccessDestination The destination screen to navigate to after successful transfer.
 */
fun NavController.navigateToTransferProcessScreen(
    transferPayload: ReviewTransferPayload,
    transferType: TransferType,
    transferSuccessDestination: String,
) {
    this.navigate(
        TransferProcessRoute(
            fromAccountId = transferPayload.payFromAccount?.accountNo,
            fromClientId = transferPayload.payFromAccount?.clientId,
            fromAccountType = transferPayload.payFromAccount?.accountType?.id,
            fromOfficeId = transferPayload.payFromAccount?.officeId,
            fromClientName = transferPayload.payFromAccount?.clientName,
            toOfficeId = transferPayload.payToAccount?.officeId,
            toAccountId = transferPayload.payToAccount?.accountNo,
            toClientId = transferPayload.payToAccount?.clientId,
            toAccountType = transferPayload.payToAccount?.accountType?.id,
            toClientName = transferPayload.payToAccount?.clientName,
            transferAmount = transferPayload.amount,
            transferDescription = transferPayload.review,
            transferType = transferType.name,
            transferSuccessDestination = transferSuccessDestination,
        ),
    )
}
