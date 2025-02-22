/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.feature.accounts.screen.AccountsScreen

fun NavController.navigateToAccountsScreen(accountType: AccountType = AccountType.SAVINGS) {
    navigate(AccountsNavigation.AccountsScreen.passArguments(accountType = accountType))
}

fun NavGraphBuilder.accountsNavGraph(
    navController: NavController,
    navigateToAccountDetail: (AccountType, Long) -> Unit,
    navigateToLoanApplicationScreen: () -> Unit,
    navigateToSavingsApplicationScreen: () -> Unit,
) {
    navigation(
        startDestination = AccountsNavigation.AccountsScreen.route,
        route = AccountsNavigation.AccountsBase.route,
    ) {
        accountsScreenRoute(
            navigateBack = navController::popBackStack,
            navigateToAccountDetail = navigateToAccountDetail,
            navigateToLoanApplicationScreen = navigateToLoanApplicationScreen,
            navigateToSavingsApplicationScreen = navigateToSavingsApplicationScreen,
        )
    }
}

fun NavGraphBuilder.accountsScreenRoute(
    navigateBack: () -> Unit,
    navigateToAccountDetail: (AccountType, Long) -> Unit,
    navigateToLoanApplicationScreen: () -> Unit,
    navigateToSavingsApplicationScreen: () -> Unit,
) {
    composable(
        route = AccountsNavigation.AccountsScreen.route,
        arguments = listOf(
            navArgument(name = Constants.ACCOUNT_TYPE) {
                type = NavType.StringType
            },
        ),
    ) {
        AccountsScreen(
            navigateBack = navigateBack,
            navigateToLoanApplicationScreen = navigateToLoanApplicationScreen,
            navigateToSavingsApplicationScreen = navigateToSavingsApplicationScreen,
            onAccountClicked = { accountType, accountId ->
                navigateToAccountDetail(
                    accountType,
                    accountId,
                )
            },
        )
    }
}
