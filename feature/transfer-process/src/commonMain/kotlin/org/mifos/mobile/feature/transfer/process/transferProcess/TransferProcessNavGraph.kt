/*
 * Copyright 2024 Mifos Initiative
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

@Serializable
data class TransferProcessRoute(
    val fromOfficeId: Int? = null,
    val fromClientId: Long? = null,
    val fromAccountType: Int? = null,
    val fromAccountId: String? = null,
    val toOfficeId: Int? = null,
    val toClientId: Long? = null,
    val toAccountType: Int? = null,
    val toAccountId: String? = null,
    val transferAmount: String? = null,
    val transferDescription: String? = null,
    val transferType: String = TransferType.SELF.name,
    val transferSuccessDestination: String = "",
)

@Serializable
data class BillRoute(
    val transferId: String,
    val amount: String,
    val fromAccount: String,
    val toAccount: String,
    val date: String,
    val remark: String = "",
)

fun NavGraphBuilder.transferProcessDestination(
    navigateBack: () -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateToBillScreen: (String, String, String, String, String, String) -> Unit,
) {
    composableWithSlideTransitions<TransferProcessRoute> {
        TransferProcessScreen(
            navigateBack = navigateBack,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
            navigateToStatusScreen = navigateToStatusScreen,
            navigateToBillScreen = navigateToBillScreen,
        )
    }
}

fun NavGraphBuilder.billDestination(
    navigateToHome: () -> Unit,
) {
    composableWithSlideTransitions<BillRoute> {
        BillScreen(
            navigateToHome = navigateToHome,
        )
    }
}

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
            toOfficeId = transferPayload.payToAccount?.officeId,
            toAccountId = transferPayload.payToAccount?.accountNo,
            toClientId = transferPayload.payToAccount?.clientId,
            toAccountType = transferPayload.payToAccount?.accountType?.id,
            transferAmount = transferPayload.amount,
            transferDescription = transferPayload.review,
            transferType = transferType.name,
            transferSuccessDestination = transferSuccessDestination,
        ),
    )
}

fun NavController.navigateToBillScreen(
    transferId: String,
    amount: String,
    fromAccount: String,
    toAccount: String,
    date: String,
    remark: String,
) {
    this.navigate(
        BillRoute(
            transferId = transferId,
            amount = amount,
            fromAccount = fromAccount,
            toAccount = toAccount,
            date = date,
            remark = remark,
        ),
    )
}
