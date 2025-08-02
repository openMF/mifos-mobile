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

package org.mifos.mobile.feature.savingsaccount.savingsAccountDetails

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_FROM
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_TO
import org.mifos.mobile.core.model.entity.AccountDetails
import org.mifos.mobile.core.model.entity.TransferArgs
import org.mifos.mobile.core.model.entity.TransferSuccessDestination
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.composableWithSlideTransitions

@Serializable
data class SavingsAccountDetailsRoute(
    val accountId: Long,
)

fun NavController.navigateToSavingsAccountDetailsScreen(accountId: Long, navOptions: NavOptions? = null) =
    navigate(SavingsAccountDetailsRoute(accountId), navOptions)

fun NavGraphBuilder.savingsAccountDetailsDestination(
    navigateBack: () -> Unit,
    navigateToDepositScreen: (TransferArgs) -> Unit,
    navigateToTransferScreen: (TransferArgs) -> Unit,
    navigateToClientChargeScreen: (String, Long) -> Unit,
    navigateToUpdateScreen: (Long, String?, String?, String?, String?) -> Unit,
    navigateToSavingsAccountTransactionScreen: (Long) -> Unit,
    navigateToWithdrawScreen: (Long, String?, String?, String?, String?) -> Unit,
    navigateToQrCodeScreen: (String) -> Unit,
) {
    composableWithSlideTransitions<SavingsAccountDetailsRoute> {
        SavingsAccountDetailsScreen(
            navigateBack = navigateBack,
            navigateToClientChargeScreen = navigateToClientChargeScreen,
            navigateToUpdateScreen = navigateToUpdateScreen,
            navigateToWithdrawScreen = navigateToWithdrawScreen,
            navigateToSavingsAccountTransactionScreen = navigateToSavingsAccountTransactionScreen,
            navigateToQrCodeScreen = navigateToQrCodeScreen,
            navigateToDepositScreen = {
                val args = TransferArgs(
                    transferPayloadJson = Json.encodeToString(
                        AccountDetails(
                            accountId = it,
                            transferType = TRANSFER_PAY_TO,
                            transferTarget = TransferType.TPT,
                            transferSuccessDestination = TransferSuccessDestination.SAVINGS_ACCOUNT,
                        ),
                    ),
                )
                navigateToDepositScreen(args)
            },
            navigateToTransferScreen = {
                val args = TransferArgs(
                    transferPayloadJson = Json.encodeToString(
                        AccountDetails(
                            accountId = it,
                            transferType = TRANSFER_PAY_FROM,
                            transferTarget = TransferType.TPT,
                            transferSuccessDestination = TransferSuccessDestination.SAVINGS_ACCOUNT,
                        ),
                    ),
                )
                navigateToTransferScreen(args)
            },
        )
    }
}
