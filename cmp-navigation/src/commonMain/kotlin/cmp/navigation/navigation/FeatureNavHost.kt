/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import cmp.navigation.callHelpline
import cmp.navigation.mailHelpline
import cmp.navigation.ui.AppState
import org.mifos.mobile.core.model.entity.TransferSuccessDestination
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.feature.about.navigation.aboutUsNavGraph
import org.mifos.mobile.feature.about.navigation.navigateToAboutUsScreen
import org.mifos.mobile.feature.accounts.navigation.AccountsNavigation
import org.mifos.mobile.feature.accounts.navigation.accountsNavGraph
import org.mifos.mobile.feature.accounts.navigation.navigateToAccountsScreen
import org.mifos.mobile.feature.charge.navigation.clientChargeNavGraph
import org.mifos.mobile.feature.charge.navigation.navigateToClientChargeScreen
import org.mifos.mobile.feature.help.navigation.helpNavGraph
import org.mifos.mobile.feature.help.navigation.navigateToHelpScreen
import org.mifos.mobile.feature.home.navigation.HomeDestinations
import org.mifos.mobile.feature.home.navigation.HomeNavigation
import org.mifos.mobile.feature.home.navigation.homeNavGraph
import org.mifos.mobile.feature.home.navigation.navigateToHomeScreen
import org.mifos.mobile.feature.loan.navigation.loanNavGraph
import org.mifos.mobile.feature.loan.navigation.navigateToLoanApplication
import org.mifos.mobile.feature.loan.navigation.navigateToLoanDetailScreen
import org.mifos.mobile.feature.third.party.transfer.navigation.navigateToThirdPartyTransfer
import org.mifos.mobile.feature.third.party.transfer.navigation.thirdPartyTransferNavGraph
import org.mifos.mobile.feature.transfer.process.navigation.navigateToTransferProcessScreen
import org.mifos.mobile.feature.transfer.process.navigation.transferProcessNavGraph
import org.mifos.mobile.feature.update.password.navigation.updatePasswordNavGraph

@Composable
internal fun FeatureNavHost(
    appState: AppState,
    onClickLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        route = NavGraphRoute.MAIN_GRAPH,
        startDestination = HomeNavigation.HomeBase.route,
        navController = appState.navController,
        modifier = modifier,
    ) {
        helpNavGraph(
            findLocations = {},
            navigateBack = appState.navController::popBackStack,
            callHelpline = {},
            mailHelpline = {},
        )
        homeNavGraph(
            onNavigate = { handleHomeNavigation(appState.navController, it, onClickLogout) },
            callHelpline = { callHelpline() },
            mailHelpline = { mailHelpline() },
        )

        accountsNavGraph(
            navController = appState.navController,
            navigateToLoanApplicationScreen = appState.navController::navigateToLoanApplication,
            navigateToSavingsApplicationScreen = { },
            navigateToAccountDetail = { accountType, id ->
                when (accountType) {
                    AccountType.SAVINGS -> { }
                    AccountType.LOAN ->
                        appState.navController.navigateToLoanDetailScreen(loanId = id)
                    AccountType.SHARE -> { }
                }
            },
        )

        aboutUsNavGraph(navController = appState.navController, navigateToOssLicense = { })

        loanNavGraph(
            navController = appState.navController,
            viewQr = { },
            viewGuarantor = { },
            viewCharges = { chargeType, chargeTypeId ->
                appState.navController.navigateToClientChargeScreen(chargeType, chargeTypeId)
            },
            makePayment = { _, _, _, _ -> },
        )

        clientChargeNavGraph(
            navigateBack = { appState.navController.popBackStack() },
        )

        thirdPartyTransferNavGraph(
            navigateBack = { appState.navController.popBackStack() },
            addBeneficiary = { },
            reviewTransfer = { transferPayload, transferType, transferDestination ->
                appState.navController.navigateToTransferProcessScreen(
                    transferPayload,
                    transferType,
                    transferDestination,
                )
            },
        )

        updatePasswordNavGraph {
            appState.navController.popBackStack()
        }

        transferProcessNavGraph(
            navigateBack = { appState.navController.popBackStack() },
            onTransferSuccessNavigate = { destination ->
                when (destination) {
                    TransferSuccessDestination.HOME -> appState.navController.navigateToHomeScreen()
                    TransferSuccessDestination.LOAN_ACCOUNT ->
                        appState.navController.navigateToAccountsScreen(
                            AccountType.LOAN,
                            AccountsNavigation.AccountsBase.route,
                        )

                    TransferSuccessDestination.SAVINGS_ACCOUNT -> appState.navController.navigateToAccountsScreen(
                        AccountType.SAVINGS,
                        AccountsNavigation.AccountsBase.route,
                    )
                }
            },
        )
    }
}

fun handleHomeNavigation(
    navController: NavHostController,
    homeDestinations: HomeDestinations,
    onClickLogout: () -> Unit,
) {
    when (homeDestinations) {
        HomeDestinations.LOGOUT -> onClickLogout.invoke()
        HomeDestinations.HOME -> Unit
        HomeDestinations.ACCOUNTS -> navController.navigateToAccountsScreen()
        HomeDestinations.LOAN_ACCOUNT -> navController.navigateToAccountsScreen(accountType = AccountType.LOAN)
        HomeDestinations.SAVINGS_ACCOUNT -> navController.navigateToAccountsScreen(accountType = AccountType.SAVINGS)
        HomeDestinations.RECENT_TRANSACTIONS -> { }
        HomeDestinations.CHARGES -> navController.navigateToClientChargeScreen(ChargeType.CLIENT, -1L)
        HomeDestinations.THIRD_PARTY_TRANSFER -> navController.navigateToThirdPartyTransfer()
        HomeDestinations.SETTINGS -> { }
        HomeDestinations.ABOUT_US -> navController.navigateToAboutUsScreen()
        HomeDestinations.HELP -> navController.navigateToHelpScreen()
        HomeDestinations.SHARE -> { }
        HomeDestinations.APP_INFO -> { }
        HomeDestinations.TRANSFER -> { }
        HomeDestinations.BENEFICIARIES -> { }
        HomeDestinations.SURVEY -> { }
        HomeDestinations.NOTIFICATIONS -> { }
        HomeDestinations.PROFILE -> { }
    }
}
