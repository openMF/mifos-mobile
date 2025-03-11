/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transfer.process.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.common.DateHelper.currentDate
import org.mifos.mobile.core.model.entity.TransferSuccessDestination
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.feature.transfer.process.TransferProcessScreen

// Navigation Setup
fun NavController.navigateToTransferProcessScreen(
    transferPayload: ReviewTransferPayload,
    transferType: TransferType,
    transferSuccessDestination: TransferSuccessDestination,
) {
    navigate(
        TransferProcessNavigation.TransferProcessScreen.passArguments(
            transferType = transferType,
            payload = transferPayload.convertToTransferPayloadString(),
            transferSuccessDestination = transferSuccessDestination,
        ),
    )
}

fun NavGraphBuilder.transferProcessNavGraph(
    navigateBack: () -> Unit,
    onTransferSuccessNavigate: (TransferSuccessDestination) -> Unit = {},
) {
    navigation(
        startDestination = TransferProcessNavigation.TransferProcessScreen.route,
        route = TransferProcessNavigation.TransferProcessBase.route,
    ) {
        transferProcessScreenRoute(
            navigateBack = navigateBack,
            onTransferSuccessNavigate = onTransferSuccessNavigate,
        )
    }
}

private fun NavGraphBuilder.transferProcessScreenRoute(
    navigateBack: () -> Unit,
    onTransferSuccessNavigate: (TransferSuccessDestination) -> Unit = {},
) {
    composable(
        route = TransferProcessNavigation.TransferProcessScreen.route,
        arguments = listOf(
            navArgument(name = Constants.PAYLOAD) { type = NavType.StringType },
            navArgument(name = Constants.TRANSFER_TYPE) { type = NavType.StringType },
            navArgument(name = Constants.TRANSFER_SUCCESS_DESTINATION) { type = NavType.StringType },
        ),
    ) {
        TransferProcessScreen(
            navigateBack = navigateBack,
            onTransferSuccessNavigate = onTransferSuccessNavigate,

        )
    }
}

private fun ReviewTransferPayload.convertToTransferPayloadString(): String {
    val transferDate = listOf(
        currentDate.dayOfMonth,
        currentDate.monthNumber,
        currentDate.year,
    )
    val transferPayload = TransferPayload(
        fromAccountId = this.payFromAccount?.accountNo,
        fromClientId = this.payFromAccount?.clientId,
        fromAccountType = this.payFromAccount?.accountType?.id,
        fromOfficeId = this.payFromAccount?.officeId,
        toOfficeId = this.payFromAccount?.officeId,
        toAccountId = this.payToAccount?.accountNo,
        toClientId = this.payToAccount?.clientId,
        toAccountType = this.payToAccount?.accountType?.id,
        transferDate = DateHelper.getDateMonthYearString(transferDate),
        transferAmount = this.amount.toDoubleOrNull(),
        transferDescription = this.review,
        dateFormat = "dd MMMM yyyy",
        locale = "en",
    )

    return Json.encodeToString(transferPayload)
}
