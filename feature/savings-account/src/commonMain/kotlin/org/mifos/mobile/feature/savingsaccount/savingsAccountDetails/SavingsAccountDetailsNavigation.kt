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
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_FROM
import org.mifos.mobile.core.model.StatusNavigationDestination
import org.mifos.mobile.core.model.entity.AccountDetails
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * Type-safe, serializable route for the Savings Account Details screen.
 *
 * @property accountId The unique ID of the savings account to display.
 */
@Serializable
data class SavingsAccountDetailsRoute(
    val accountId: Long,
)

/**
 * Navigates to the Savings Account Details screen.
 *
 * @param accountId The ID of the savings account.
 * @param navOptions Optional navigation options.
 */

fun NavController.navigateToSavingsAccountDetailsScreen(
    accountId: Long,
    navOptions: NavOptions? = null,
) =
    navigate(SavingsAccountDetailsRoute(accountId), navOptions)

/**
 * Defines the composable destination for the "Savings Account Details" screen.
 * This sets up the route, content, and navigation callbacks.
 *
 * @param navigateBack Handles back navigation.
 * @param navigateToTransferScreen Navigates to the fund transfer screen.
 * @param navigateToClientChargeScreen Navigates to the account charges screen.
 * @param navigateToUpdateScreen Navigates to the account update screen.
 * @param navigateToSavingsAccountTransactionScreen Navigates to the transaction history.
 * @param navigateToQrCodeScreen Navigates to the QR code display screen.
 */
fun NavGraphBuilder.savingsAccountDetailsDestination(
    navigateBack: () -> Unit,
    navigateToTransferScreen: (AccountDetails) -> Unit,
    navigateToClientChargeScreen: (String, Long) -> Unit,
    navigateToUpdateScreen: (Long, String?, String?, String?, String?) -> Unit,
    navigateToSavingsAccountTransactionScreen: (Long) -> Unit,
    navigateToQrCodeScreen: (String) -> Unit,
) {
    composableWithSlideTransitions<SavingsAccountDetailsRoute> {
        SavingsAccountDetailsScreen(
            navigateBack = navigateBack,
            navigateToClientChargeScreen = navigateToClientChargeScreen,
            navigateToUpdateScreen = navigateToUpdateScreen,
            navigateToSavingsAccountTransactionScreen = navigateToSavingsAccountTransactionScreen,
            navigateToQrCodeScreen = navigateToQrCodeScreen,
            navigateToTransferScreen = {
                val args = AccountDetails(
                    accountId = it,
                    transferType = TRANSFER_PAY_FROM,
                    transferTarget = TransferType.SELF,
                    transferSuccessDestination = StatusNavigationDestination.SAVINGS_ACCOUNT.name,
                )
                navigateToTransferScreen(args)
            },
        )
    }
}
