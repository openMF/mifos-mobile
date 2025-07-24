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

package org.mifos.mobile.feature.accounts.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions
import org.mifos.mobile.feature.accounts.screen.AccountsScreen

@Serializable
data class AccountNavRoute(
    val accountType: String,
)

fun NavController.navigateToAccountsScreen(
    accountType: String,
    navOptions: NavOptions? = null,
) {
    this.navigate(AccountNavRoute(accountType), navOptions)
}

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
