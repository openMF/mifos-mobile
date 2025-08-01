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

package org.mifos.mobile.feature.loanaccount.loanAccountDetails

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.entity.TransferArgs
import org.mifos.mobile.core.ui.composableWithSlideTransitions

@Serializable
data class LoanAccountDetailsRoute(
    val accountId: Long,
)

fun NavController.navigateToLoanAccountDetailsScreen(accountId: Long, navOptions: NavOptions? = null) =
    navigate(LoanAccountDetailsRoute(accountId), navOptions)

fun NavGraphBuilder.loanAccountDetailsDestination(
    navigateBack: () -> Unit,
    navigateToMakePaymentScreen: (args: TransferArgs) -> Unit,
    navigateToRepaymentScheduleScreen: (Long) -> Unit,
    navigateToLoanSummaryScreen: (Long) -> Unit,
    navigateToQrCodeScreen: (String) -> Unit,
    navigateToClientChargeScreen: (String, Long) -> Unit,
    navigateToLoanAccountTransactionScreen: (Long) -> Unit,
) {
    composableWithSlideTransitions<LoanAccountDetailsRoute> {
        LoanAccountDetailsScreen(
            navigateBack = navigateBack,
            navigateToClientChargeScreen = navigateToClientChargeScreen,
            navigateToMakePaymentScreen = navigateToMakePaymentScreen,
            navigateToRepaymentScheduleScreen = navigateToRepaymentScheduleScreen,
            navigateToLoanSummaryScreen = navigateToLoanSummaryScreen,
            navigateToQrCodeScreen = navigateToQrCodeScreen,
            navigateToLoanAccountTransactionScreen = navigateToLoanAccountTransactionScreen,
        )
    }
}
