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

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.google.gson.Gson
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.utils.DateHelper
import org.mifos.mobile.core.common.utils.getTodayFormatted
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.feature.transfer.process.TransferProcessScreen

// Navigation Setup
fun NavController.navigateToTransferProcessScreen(
    transferPayload: ReviewTransferPayload,
    transferType: TransferType,
) {
    Log.d("Tag-navController", "navigateToTransferProcessScreen: $transferPayload")
    navigate(
        TransferProcessNavigation.TransferProcessScreen.passArguments(
            transferType = transferType,
            payload = transferPayload.convertToTransferPayloadString(),
        ),
    )
}

fun NavGraphBuilder.transferProcessNavGraph(
    navigateBack: () -> Unit,
) {
    navigation(
        startDestination = TransferProcessNavigation.TransferProcessScreen.route,
        route = TransferProcessNavigation.TransferProcessBase.route,
    ) {
        transferProcessScreenRoute(
            navigateBack = navigateBack,
        )
    }
}

private fun NavGraphBuilder.transferProcessScreenRoute(
    navigateBack: () -> Unit,
) {
    composable(
        route = TransferProcessNavigation.TransferProcessScreen.route,
        arguments = listOf(
            navArgument(name = Constants.PAYLOAD) { type = NavType.StringType },
            navArgument(name = Constants.TRANSFER_TYPE) {
                type = NavType.EnumType(TransferType::class.java)
            },
        ),
    ) {
        TransferProcessScreen(
            navigateBack = navigateBack,
        )
    }
}

private fun ReviewTransferPayload.convertToTransferPayloadString(): String {
    val payload = this
    val transferPayload = TransferPayload().apply {
        fromAccountId = payload.payFromAccount?.accountId
        fromClientId = payload.payFromAccount?.clientId
        fromAccountType = payload.payFromAccount?.accountType?.id
        fromOfficeId = payload.payFromAccount?.officeId
        fromAccountNumber = payload.payFromAccount?.accountNo
//        toOfficeId = payload.payFromAccount?.officeId
        toOfficeId = payload.payToAccount?.officeId
        toAccountId = payload.payToAccount?.accountId
        toClientId = payload.payToAccount?.clientId
        toAccountType = payload.payToAccount?.accountType?.id
        toAccountNumber = payload.payToAccount?.accountNo
        transferDate = DateHelper.getSpecificFormat(DateHelper.FORMAT_MMMM, getTodayFormatted())
        transferAmount = payload.amount.toDoubleOrNull()
        transferDescription = payload.review
        fromAccountNumber = payload.payFromAccount?.accountNo
        toAccountNumber = payload.payToAccount?.accountNo
    }

    val gson = Gson()
    return gson.toJson(transferPayload)
}
