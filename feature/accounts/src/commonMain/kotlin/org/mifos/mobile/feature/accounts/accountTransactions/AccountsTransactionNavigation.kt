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

package org.mifos.mobile.feature.accounts.accountTransactions

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * Navigation route for the Account Transactions Screen.
 *
 * @param accountType The type of account to be displayed.
 * @param accountId The ID of the account to be displayed.
 */

@Serializable
data class AccountTransactionsNavRoute(
    val accountType: String,
    val accountId: Long,
)

/**
 * Navigates to the Account Transactions Screen with the specified account type and ID.
 *
 * @param accountType The type of account to be displayed.
 * @param accountId The ID of the account to be displayed.
 * @param navOptions Optional navigation options for the transaction screen.
 */
fun NavController.navigateToAccountTransactionsScreen(
    accountType: String,
    accountId: Long,
    navOptions: NavOptions? = null,
) {
    this.navigate(AccountTransactionsNavRoute(accountType, accountId), navOptions)
}

/**
 * Adds the Account Transactions Screen to the navigation graph.
 *
 * @param navigateBack The function to be called when the back button is pressed.
 */
fun NavGraphBuilder.accountTransactionsDestination(
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<AccountTransactionsNavRoute> {
        TransactionScreen(navigateBack)
    }
}
