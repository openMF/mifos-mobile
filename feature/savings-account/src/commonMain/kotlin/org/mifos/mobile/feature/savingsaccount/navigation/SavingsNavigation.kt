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

package org.mifos.mobile.feature.savingsaccount.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.entity.AccountDetails
import org.mifos.mobile.feature.savingsaccount.savingsAccount.SavingsAccountRoute
import org.mifos.mobile.feature.savingsaccount.savingsAccount.savingsAccountDestination
import org.mifos.mobile.feature.savingsaccount.savingsAccountDetails.savingsAccountDetailsDestination
import org.mifos.mobile.feature.savingsaccount.savingsAccountUpdate.navigateToSavingsAccountUpdateScreen
import org.mifos.mobile.feature.savingsaccount.savingsAccountUpdate.savingsAccountUpdateDestination
import org.mifos.mobile.feature.savingsaccount.savingsAccountWithdraw.savingsAccountWithdrawDestination

/**
 * A serializable, type-safe route representing the entry point for the nested
 * savings account navigation graph.
 */
@Serializable
data object SavingsGraphRoute

/**
 * Navigates to the savings account navigation graph.
 *
 * This is a convenience extension function on [NavController] that encapsulates the
 * logic for navigating to the start of the savings account feature.
 *
 * @param navOptions Optional [NavOptions] to apply to this navigation operation.
 */
fun NavController.navigateToSavingsGraph(navOptions: NavOptions? = null) =
    navigate(SavingsAccountRoute, navOptions)

/**
 * Builds the nested navigation graph for the savings account feature.
 *
 * This function defines all the destinations within the savings account module and
 * wires them together. It takes lambdas for navigating to screens both inside and
 * outside of this feature's scope, promoting a decoupled architecture.
 *
 * @param navController The [NavController] used for handling navigation events within the graph.
 * @param navigateToClientChargeScreen Lambda to navigate to the client charges screen.
 * @param navigateToTransferScreen Lambda to navigate to the fund transfer screen.
 * @param navigateToAuthenticateScreen Lambda to navigate to an authentication screen.
 * @param navigateToStatusScreen Lambda to navigate to a generic status/result screen after an operation.
 * @param navigateToSavingsAccountTransactionScreen Lambda to navigate to the transaction history screen.
 * @param navigateToQrCodeScreen Lambda to navigate to the QR code display screen.
 */
fun NavGraphBuilder.savingsNavGraph(
    navController: NavController,
    navigateToClientChargeScreen: (String, Long) -> Unit,
    navigateToTransferScreen: (AccountDetails) -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateToSavingsAccountTransactionScreen: (Long) -> Unit,
    navigateToQrCodeScreen: (String) -> Unit,
) {
    navigation<SavingsGraphRoute>(
        startDestination = SavingsAccountRoute,
    ) {
        // Destination for the list of savings accounts
        savingsAccountDestination(
            navigateBack = navController::popBackStack,
        )
        // Destination for the details of a single savings account
        savingsAccountDetailsDestination(
            navigateBack = navController::popBackStack,
            navigateToClientChargeScreen = navigateToClientChargeScreen,
            navigateToUpdateScreen =
            navController::navigateToSavingsAccountUpdateScreen,
            navigateToSavingsAccountTransactionScreen =
            navigateToSavingsAccountTransactionScreen,
            navigateToQrCodeScreen = navigateToQrCodeScreen,
            navigateToTransferScreen = navigateToTransferScreen,
        )
        // Destination for updating a savings account (e.g., deposit)
        savingsAccountUpdateDestination(
            navigateBack = navController::popBackStack,
            navigateToStatusScreen = navigateToStatusScreen,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
        )
        // Destination for withdrawing from a savings account
        savingsAccountWithdrawDestination(
            navigateBack = navController::popBackStack,
            navigateToStatusScreen = navigateToStatusScreen,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
        )
    }
}
