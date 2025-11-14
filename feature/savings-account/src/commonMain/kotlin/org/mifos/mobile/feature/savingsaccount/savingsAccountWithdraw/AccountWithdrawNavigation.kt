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

package org.mifos.mobile.feature.savingsaccount.savingsAccountWithdraw

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * A type-safe, serializable route for the Savings Account Withdraw screen.
 *
 * This class encapsulates all the necessary parameters for the withdrawal screen,
 * such as account and client details, making navigation more robust and less
 * prone to errors. The parameters are optional where applicable.
 *
 * @property accountId The unique identifier of the savings account from which to withdraw.
 * @property clientName The name of the client who owns the account (optional).
 * @property submissionData The submission date of the account (optional).
 * @property accountNumber The account number (optional).
 * @property product The name of the savings product (optional).
 */
@Serializable
data class SavingsAccountWithdrawRoute(
    val accountId: Long,
    val clientName: String? = null,
    val submissionData: String? = null,
    val accountNumber: String? = null,
    val product: String? = null,
)

/**
 * Navigates to the Savings Account Withdraw screen.
 *
 * This is an extension function on [NavController] that simplifies the process of
 * navigating to the withdrawal screen by constructing and passing the
 * [SavingsAccountWithdrawRoute] with all required and optional parameters.
 *
 * @param accountId The ID of the account to withdraw from.
 * @param clientName The name of the client.
 * @param submissionData The account's submission date.
 * @param accountNumber The account number.
 * @param product The savings product name.
 * @param navOptions Optional [NavOptions] to apply to this navigation operation.
 */
fun NavController.navigateToSavingsAccountWithdrawScreen(
    accountId: Long,
    clientName: String?,
    submissionData: String?,
    accountNumber: String?,
    product: String?,
    navOptions: NavOptions? = null,
) =
    navigate(
        SavingsAccountWithdrawRoute(
            accountId,
            clientName,
            submissionData,
            accountNumber,
            product,
        ),
        navOptions,
    )

/**
 * Defines the composable destination for the "Savings Account Withdraw" screen
 * within the navigation graph.
 *
 * This function sets up the route, the screen content (`AccountWithdrawScreen`),
 * and specifies the screen transitions. It also wires up the necessary navigation
 * callbacks for actions initiated from the withdrawal screen.
 *
 * @param navigateBack A lambda function to handle the back navigation event.
 * @param navigateToAuthenticateScreen A lambda to navigate to an authentication screen,
 *   typically required before performing a sensitive action like a withdrawal.
 * @param navigateToStatusScreen A lambda to navigate to a generic status/result screen
 *   after the withdrawal operation is complete, passing along relevant details.
 */
fun NavGraphBuilder.savingsAccountWithdrawDestination(
    navigateBack: () -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
) {
    composableWithSlideTransitions<SavingsAccountWithdrawRoute> {
        AccountWithdrawScreen(
            navigateBack = navigateBack,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
            navigateToStatusScreen = navigateToStatusScreen,
        )
    }
}
