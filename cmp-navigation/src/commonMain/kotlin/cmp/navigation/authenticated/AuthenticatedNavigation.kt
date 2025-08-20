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

package cmp.navigation.authenticated

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import cmp.navigation.authenticatednavbar.AuthenticatedNavbarRoute
import cmp.navigation.authenticatednavbar.authenticatedNavbarGraph
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.model.entity.TransferSuccessDestination
import org.mifos.mobile.feature.accounts.accountTransactions.accountTransactionsDestination
import org.mifos.mobile.feature.accounts.accountTransactions.navigateToAccountTransactionsScreen
import org.mifos.mobile.feature.accounts.accounts.accountsDestination
import org.mifos.mobile.feature.accounts.accounts.navigateToAccountsScreen
import org.mifos.mobile.feature.auth.login.navigateToLoginScreen
import org.mifos.mobile.feature.auth.navigation.AuthGraphRoute
import org.mifos.mobile.feature.beneficiary.beneficiaryApplication.navigateToManualBeneficiaryAddScreen
import org.mifos.mobile.feature.beneficiary.navigation.beneficiaryNavGraph
import org.mifos.mobile.feature.beneficiary.navigation.navigateToBeneficiaryNavGraph
import org.mifos.mobile.feature.charge.charges.navigateToClientChargeScreen
import org.mifos.mobile.feature.charge.navigation.clientChargeNavGraph
import org.mifos.mobile.feature.charge.navigation.navigateToChargeGraph
import org.mifos.mobile.feature.home.navigation.HomeNavigationDestination
import org.mifos.mobile.feature.loan.application.navigation.loanApplicationNavGraph
import org.mifos.mobile.feature.loan.application.navigation.navigateToLoanApplicationGraph
import org.mifos.mobile.feature.loanaccount.loanAccountDetails.navigateToLoanAccountDetailsScreen
import org.mifos.mobile.feature.loanaccount.navigation.loanNavGraph
import org.mifos.mobile.feature.location.navigation.locationsNavGraph
import org.mifos.mobile.feature.notification.navigation.navigateToNotificationScreen
import org.mifos.mobile.feature.notification.navigation.notificationDestination
import org.mifos.mobile.feature.passcode.navigation.PasscodeRoute
import org.mifos.mobile.feature.passcode.verifyPasscode.navigateToVerifyPasscodeScreen
import org.mifos.mobile.feature.passcode.verifyPasscode.passcodeDestination
import org.mifos.mobile.feature.qr.navigation.qrNavGraph
import org.mifos.mobile.feature.qr.qr.navigateToQrReaderScreen
import org.mifos.mobile.feature.qr.qrCodeDisplay.navigateToQrDisplayScreen
import org.mifos.mobile.feature.recent.transaction.navigation.recentTransactionNavGraph
import org.mifos.mobile.feature.savings.application.navigation.navigateToSavingsApplicationGraph
import org.mifos.mobile.feature.savings.application.navigation.savingsApplicationNavGraph
import org.mifos.mobile.feature.savingsaccount.navigation.savingsNavGraph
import org.mifos.mobile.feature.savingsaccount.savingsAccountDetails.navigateToSavingsAccountDetailsScreen
import org.mifos.mobile.feature.settings.faq.faqDestination
import org.mifos.mobile.feature.settings.faq.navigateToFaq
import org.mifos.mobile.feature.share.application.navigation.navigateToShareApplicationGraph
import org.mifos.mobile.feature.share.application.navigation.shareApplicationNavGraph
import org.mifos.mobile.feature.status.navigation.StatusNavigationRoute
import org.mifos.mobile.feature.status.navigation.statusDestination
import org.mifos.mobile.feature.third.party.transfer.navigation.thirdPartyTransferNavGraph
import org.mifos.mobile.feature.transfer.process.makeTransfer.makeTransferDestination
import org.mifos.mobile.feature.transfer.process.makeTransfer.navigateToMakeTransferScreen
import org.mifos.mobile.feature.transfer.process.transferProcess.navigateToTransferProcessScreen
import org.mifos.mobile.feature.transfer.process.transferProcess.transferProcessDestination

@Serializable
internal data object AuthenticatedGraphRoute

internal fun NavController.navigateToAuthenticatedGraph(navOptions: NavOptions? = null) {
    navigate(route = AuthenticatedGraphRoute, navOptions = navOptions)
}

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
internal fun NavGraphBuilder.authenticatedGraph(
    navController: NavController,
) {
    navigation<AuthenticatedGraphRoute>(
        startDestination = AuthenticatedNavbarRoute,
    ) {
        authenticatedNavbarGraph { destination ->
            when (destination) {
                is HomeNavigationDestination.AccountsWithType -> {
                    if (destination.type in listOf(
                            Constants.SAVINGS_ACCOUNT,
                            Constants.LOAN_ACCOUNT,
                            Constants.SHARE_ACCOUNTS,
                        )
                    ) {
                        navController.navigateToAccountsScreen(destination.type)
                    }
                }

                is HomeNavigationDestination.Notification ->
                    navController.navigateToNotificationScreen()

                is HomeNavigationDestination.Charge ->
                    navController.navigateToChargeGraph()

                is HomeNavigationDestination.Faq ->
                    navController.navigateToFaq()

                is HomeNavigationDestination.Beneficiary ->
                    navController.navigateToBeneficiaryNavGraph()

                is HomeNavigationDestination.Transaction ->
                    navController.navigateToAccountTransactionsScreen(
                        Constants.RECENT_TRANSACTIONS,
                        -1L,
                    )

                is HomeNavigationDestination.ApplyLoan ->
                    navController.navigateToLoanApplicationGraph()

                is HomeNavigationDestination.ApplySavings ->
                    navController.navigateToSavingsApplicationGraph()

                is HomeNavigationDestination.ApplyShare ->
                    navController.navigateToShareApplicationGraph()
            }
        }

        notificationDestination(
            navigateBack = navController::popBackStack,
        )

        accountsDestination(
            navigateBack = navController::popBackStack,
            onAccountClicked = { accountType, accountId ->
                if (accountType == Constants.SAVINGS_ACCOUNT) {
                    navController.navigateToSavingsAccountDetailsScreen(accountId)
                } else if (accountType == Constants.LOAN_ACCOUNT) {
                    navController.navigateToLoanAccountDetailsScreen(accountId)
                }
            },
        )

        accountTransactionsDestination(
            navigateBack = navController::popBackStack,
        )

        clientChargeNavGraph(
            navController = navController,
        )

        statusDestination(
            navigateToDestination = {
                when (it) {
                    Constants.LOGIN -> {
                        navController.navigateToLoginScreen()
                    }
                    else -> {
                        navController.navigateToHomeAfterStatus()
                    }
                }
            },
        )

        savingsNavGraph(
            navController = navController,
            navigateToClientChargeScreen = navController::navigateToClientChargeScreen,
            navigateToStatusScreen = navController::navigateToStatusAfterUpdate,
            navigateToAuthenticateScreen = navController::navigateToVerifyPasscodeScreen,
            navigateToTransferScreen = {
                navController.navigateToMakeTransferScreen(it)
            },
            navigateToSavingsAccountTransactionScreen = {
                navController.navigateToAccountTransactionsScreen(Constants.SAVINGS_ACCOUNT, it)
            },
            navigateToQrCodeScreen = { navController.navigateToQrDisplayScreen(it) },
        )

        loanNavGraph(
            navController = navController,
            navigateToMakePaymentScreen = {
                navController.navigateToMakeTransferScreen(it)
            },
            navigateToQrCodeScreen = navController::navigateToQrDisplayScreen,
            navigateToClientChargeScreen = navController::navigateToClientChargeScreen,
            navigateToLoanAccountTransactionScreen = {
                navController.navigateToAccountTransactionsScreen(Constants.LOAN_ACCOUNT, it)
            },
        )

        loanApplicationNavGraph(
            navController = navController,
            navigateToAuthenticateScreen = navController::navigateToVerifyPasscodeScreen,
            navigateToStatusScreen = navController::navigateToStatusAfterUpdate,
        )

        savingsApplicationNavGraph(
            navController = navController,
            navigateToStatusScreen = navController::navigateToStatusAfterUpdate,
            navigateToAuthenticateScreen = navController::navigateToVerifyPasscodeScreen,
        )

        shareApplicationNavGraph(
            navController = navController,
            navigateToStatusScreen = navController::navigateToStatusAfterUpdate,
            navigateToAuthenticateScreen = navController::navigateToVerifyPasscodeScreen,
        )

        passcodeDestination(
            onPasscodeConfirm = navController::popBackStack,
        )

        locationsNavGraph()

        recentTransactionNavGraph(
            navController = navController,
        )

        beneficiaryNavGraph(
            navController = navController,
            navigateToQR = navController::navigateToQrReaderScreen,
            navigateToStatusScreen = navController::navigateToStatusAfterUpdate,
            navigateToAuthenticateScreen = navController::navigateToVerifyPasscodeScreen,
        )

        qrNavGraph(
            navController = navController,
            openBeneficiaryApplication = { beneficiary, state ->
                navController.navigateToManualBeneficiaryAddScreen(
                    beneficiary.id ?: -1,
                    beneficiary.clientName ?: "",
                    beneficiary.accountType?.id ?: -1,
                    beneficiary.accountNumber ?: "",
                    beneficiary.officeName ?: "",
                    state.name,
                )
            },
        )

        makeTransferDestination(
            navigateBack = navController::popBackStack,
            navigateToTransferScreen = { transferPayload, transferType, transferDestination ->
                navController.navigateToTransferProcessScreen(
                    transferPayload = transferPayload,
                    transferType = transferType,
                    transferSuccessDestination = when (transferDestination) {
                        TransferSuccessDestination.SAVINGS_ACCOUNT -> Constants.NAVIGATE_BACK_TO_SAVINGS
                        TransferSuccessDestination.LOAN_ACCOUNT -> Constants.NAVIGATE_BACK_TO_LOAN
                        TransferSuccessDestination.HOME -> ""
                    },
                )
            },
        )

        thirdPartyTransferNavGraph(
            navigateBack = navController::popBackStack,
            addBeneficiary = { },
            reviewTransfer = { transferPayload, transferType, transferDestination ->
                navController.navigateToTransferProcessScreen(
                    transferPayload,
                    transferType,
                    transferDestination.name,
                )
            },
        )

        transferProcessDestination(
            navigateBack = navController::popBackStack,
            navigateToAuthenticateScreen = navController::navigateToVerifyPasscodeScreen,
            navigateToStatusScreen = navController::navigateToStatusAfterUpdate,
        )

        faqDestination(onBackClick = navController::popBackStack, contact = {})
    }
}

@Suppress("UnusedPrivateMember")
private fun NavController.navigateUpToAuthenticatedNavbarRoot() {
    this.popBackStack<AuthenticatedNavbarRoute>(inclusive = false)
}

fun NavController.navigateToStatusScreenLoginFlow(
    eventType: String,
    eventDestination: String,
    title: String,
    subtitle: String,
    buttonText: String,
) {
    this.navigate(
        StatusNavigationRoute(
            eventType = eventType,
            eventDestination = eventDestination,
            title = title,
            subtitle = subtitle,
            buttonText = buttonText,
        ),
    ) {
        popUpTo(AuthGraphRoute) {
            inclusive = false
        }
    }
}

fun NavController.navigateToStatusScreenPasscodeFlow(
    eventType: String,
    eventDestination: String,
    title: String,
    subtitle: String,
    buttonText: String,
) {
    this.navigate(
        StatusNavigationRoute(
            eventType = eventType,
            eventDestination = eventDestination,
            title = title,
            subtitle = subtitle,
            buttonText = buttonText,
        ),
    ) {
        popUpTo(PasscodeRoute.Standard) {
            inclusive = true
        }
    }
}

fun NavController.navigateToStatusAfterUpdate(
    eventType: String,
    eventDestination: String,
    title: String,
    subtitle: String,
    buttonText: String,
) {
    this.navigate(
        StatusNavigationRoute(
            eventType = eventType,
            eventDestination = eventDestination,
            title = title,
            subtitle = subtitle,
            buttonText = buttonText,
        ),
    ) {
        popUpTo(AuthenticatedGraphRoute) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

fun NavController.navigateToHomeAfterStatus() {
    this.navigate(AuthenticatedNavbarRoute) {
        popUpTo(StatusNavigationRoute::class) {
            inclusive = true
        }
        launchSingleTop = true
    }
}
