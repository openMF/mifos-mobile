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

package org.mifos.mobile.feature.accounts.accounts

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * Navigation route for the Accounts Screen.
 *
 * @param accountType The type of account to be displayed.
 */
@Serializable
data class AccountNavRoute(
    val accountType: String,
)

/**
 * Navigates to the Accounts Screen.
 *
 * @param accountType The type of account to be displayed.
 * @param navOptions The navigation options to be applied.
 */
fun NavController.navigateToAccountsScreen(
    accountType: String,
    navOptions: NavOptions? = null,
) {
    this.navigate(AccountNavRoute(accountType), navOptions)
}

/**
 * Adds the Accounts Screen to the navigation graph.
 *
 * @param navigateBack The function to be called when the back button is pressed.
 * @param onAccountClicked The function to be called when an account is clicked.
 */
fun NavGraphBuilder.accountsDestination(
    navigateBack: () -> Unit,
    onAccountClicked: (accountType: String, accountId: Long) -> Unit,
) {
    composableWithSlideTransitions<AccountNavRoute> {
        AccountsScreen(
            navigateBack = navigateBack,
            onAccountClicked = onAccountClicked,
        )
    }
}
