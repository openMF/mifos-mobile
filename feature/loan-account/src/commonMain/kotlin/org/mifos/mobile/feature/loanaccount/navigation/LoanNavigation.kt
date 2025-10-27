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

package org.mifos.mobile.feature.loanaccount.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.entity.AccountDetails
import org.mifos.mobile.feature.loanaccount.loanAccount.LoanAccountRoute
import org.mifos.mobile.feature.loanaccount.loanAccount.loanAccountDestination
import org.mifos.mobile.feature.loanaccount.loanAccountDetails.loanAccountDetailsDestination
import org.mifos.mobile.feature.loanaccount.loanAccountRepaymentSchedule.loanAccountRepaymentDestination
import org.mifos.mobile.feature.loanaccount.loanAccountRepaymentSchedule.navigateToLoanRepaymentScreen
import org.mifos.mobile.feature.loanaccount.loanAccountSummary.loanAccountSummaryDestination
import org.mifos.mobile.feature.loanaccount.loanAccountSummary.navigateToLoanAccountSummaryScreen

@Serializable
data object LoanGraphRoute

/**
 * Navigates to the loan account navigation graph.
 *
 * @param navOptions The navigation options.
 */
fun NavController.navigateToLoanGraph(navOptions: NavOptions? = null) =
    navigate(LoanGraphRoute, navOptions)

/**
 * Defines the loan account navigation graph.
 *
 * @param navController The [NavController] for the graph.
 * @param navigateToMakePaymentScreen A callback to navigate to the make payment screen.
 * @param navigateToQrCodeScreen A callback to navigate to the QR code screen.
 * @param navigateToClientChargeScreen A callback to navigate to the client charge screen.
 * @param navigateToLoanAccountTransactionScreen A callback to navigate to the loan account transaction screen.
 */
fun NavGraphBuilder.loanNavGraph(
    navController: NavController,
    navigateToMakePaymentScreen: (AccountDetails) -> Unit,
    navigateToQrCodeScreen: (String) -> Unit,
    navigateToClientChargeScreen: (String, Long) -> Unit,
    navigateToLoanAccountTransactionScreen: (Long) -> Unit,
) {
    navigation<LoanGraphRoute>(
        startDestination = LoanAccountRoute,
    ) {
        loanAccountDestination(
            navigateBack = navController::popBackStack,
        )

        loanAccountDetailsDestination(
            navigateBack = navController::popBackStack,
            navigateToClientChargeScreen = navigateToClientChargeScreen,
            navigateToRepaymentScheduleScreen = navController::navigateToLoanRepaymentScreen,
            navigateToLoanSummaryScreen = navController::navigateToLoanAccountSummaryScreen,
            navigateToQrCodeScreen = navigateToQrCodeScreen,
            navigateToMakePaymentScreen = navigateToMakePaymentScreen,
            navigateToLoanAccountTransactionScreen = navigateToLoanAccountTransactionScreen,
        )

        loanAccountSummaryDestination(
            navigateBack = navController::popBackStack,
        )

        loanAccountRepaymentDestination(
            navigateBack = navController::popBackStack,
            navigateToMakePaymentScreen = navigateToMakePaymentScreen,
        )
    }
}
