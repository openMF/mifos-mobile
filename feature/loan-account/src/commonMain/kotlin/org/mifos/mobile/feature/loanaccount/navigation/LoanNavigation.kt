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
import org.mifos.mobile.feature.loanaccount.loanAccount.LoanAccountRoute
import org.mifos.mobile.feature.loanaccount.loanAccount.loanAccountDestination
import org.mifos.mobile.feature.loanaccount.loanAccountDetails.loanAccountDetailsDestination
import org.mifos.mobile.feature.loanaccount.loanAccountSummary.loanAccountSummaryDestination
import org.mifos.mobile.feature.loanaccount.loanAccountSummary.navigateToLoanAccountSummaryScreen

@Serializable
data object LoanGraphRoute

fun NavController.navigateToLoanGraph(navOptions: NavOptions? = null) =
    navigate(LoanGraphRoute, navOptions)

fun NavGraphBuilder.loanNavGraph(
    navController: NavController,
    navigateToMakePaymentScreen: () -> Unit,
    navigateToRepaymentScheduleScreen: (Long) -> Unit,
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
//            TODO design repayment schedule and loan summary in this module and navigate using
//             navController.navigateToRepaymentScheduleScreen()
            navigateToRepaymentScheduleScreen = navigateToRepaymentScheduleScreen,
            navigateToLoanSummaryScreen = navController::navigateToLoanAccountSummaryScreen,
            navigateToQrCodeScreen = navigateToQrCodeScreen,
            navigateToMakePaymentScreen = navigateToMakePaymentScreen,
            navigateToLoanAccountTransactionScreen = navigateToLoanAccountTransactionScreen,
        )

        loanAccountSummaryDestination(
            navigateBack = navController::popBackStack,
        )
    }
}
