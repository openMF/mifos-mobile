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

package org.mifos.mobile.feature.loanaccount.loanAccountDetails

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.entity.AccountDetails
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * A route for the loan account details screen.
 *
 * @property accountId The ID of the loan account.
 */
@Serializable
data class LoanAccountDetailsRoute(
    val accountId: Long,
)

/**
 * Navigates to the loan account details screen.
 *
 * @param accountId The ID of the loan account.
 * @param navOptions The navigation options.
 */
fun NavController.navigateToLoanAccountDetailsScreen(accountId: Long, navOptions: NavOptions? = null) =
    navigate(LoanAccountDetailsRoute(accountId), navOptions)

/**
 * Defines the loan account details screen destination in the navigation graph.
 *
 * @param navigateBack A callback to navigate back to the previous screen.
 * @param navigateToMakePaymentScreen A callback to navigate to the make payment screen.
 * @param navigateToRepaymentScheduleScreen A callback to navigate to the repayment schedule screen.
 * @param navigateToLoanSummaryScreen A callback to navigate to the loan summary screen.
 * @param navigateToQrCodeScreen A callback to navigate to the QR code screen.
 * @param navigateToClientChargeScreen A callback to navigate to the client charge screen.
 * @param navigateToLoanAccountTransactionScreen A callback to navigate to the loan account transaction screen.
 */
fun NavGraphBuilder.loanAccountDetailsDestination(
    navigateBack: () -> Unit,
    navigateToMakePaymentScreen: (AccountDetails) -> Unit,
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
