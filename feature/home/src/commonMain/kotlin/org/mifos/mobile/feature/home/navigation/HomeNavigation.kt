/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithStayTransitions
import org.mifos.mobile.feature.home.screens.HomeScreen
import org.mifos.mobile.feature.home.viewmodel.HomeCardItem
import org.mifos.mobile.feature.home.viewmodel.HomeNavigationItems

enum class HomeDestinations {
    HOME,
    ACCOUNTS,
    LOAN_ACCOUNT,
    SAVINGS_ACCOUNT,
    RECENT_TRANSACTIONS,
    CHARGES,
    THIRD_PARTY_TRANSFER,
    SETTINGS,
    ABOUT_US,
    HELP,
    SHARE,
    APP_INFO,
    LOGOUT,
    TRANSFER,
    BENEFICIARIES,
    SURVEY,
    NOTIFICATIONS,
    PROFILE,
}

fun HomeNavigationItems.toDestination(): HomeDestinations {
    return when (this) {
        HomeNavigationItems.Home -> HomeDestinations.HOME
        HomeNavigationItems.Accounts -> HomeDestinations.ACCOUNTS
        HomeNavigationItems.RecentTransactions -> HomeDestinations.RECENT_TRANSACTIONS
        HomeNavigationItems.Charges -> HomeDestinations.CHARGES
        HomeNavigationItems.ThirdPartyTransfer -> HomeDestinations.THIRD_PARTY_TRANSFER
        HomeNavigationItems.ManageBeneficiaries -> HomeDestinations.BENEFICIARIES
        HomeNavigationItems.Settings -> HomeDestinations.SETTINGS
        HomeNavigationItems.AboutUs -> HomeDestinations.ABOUT_US
        HomeNavigationItems.Help -> HomeDestinations.HELP
        HomeNavigationItems.Share -> HomeDestinations.SHARE
        HomeNavigationItems.AppInfo -> HomeDestinations.APP_INFO
        HomeNavigationItems.Logout -> HomeDestinations.LOGOUT
    }
}

fun HomeCardItem.toDestination(): HomeDestinations {
    return when (this) {
        HomeCardItem.AccountCard -> HomeDestinations.SAVINGS_ACCOUNT
        HomeCardItem.BeneficiariesCard -> HomeDestinations.BENEFICIARIES
        HomeCardItem.ChargesCard -> HomeDestinations.CHARGES
        HomeCardItem.LoanCard -> HomeDestinations.LOAN_ACCOUNT
        HomeCardItem.SurveyCard -> HomeDestinations.SURVEY
        HomeCardItem.TransferCard -> HomeDestinations.TRANSFER
    }
}

@Serializable
data object HomeRoute

fun NavController.navigateToHomeScreen(navOptions: NavOptions? = null) =
    navigate(HomeRoute, navOptions)

fun NavGraphBuilder.homeDestination(
    onNavigate: (HomeDestinations) -> Unit,
    callHelpline: () -> Unit,
    mailHelpline: () -> Unit,
) {
    composableWithStayTransitions<HomeRoute> {
        HomeScreen(
            callHelpline = callHelpline,
            mailHelpline = mailHelpline,
            onNavigate = onNavigate,
        )
    }
}
