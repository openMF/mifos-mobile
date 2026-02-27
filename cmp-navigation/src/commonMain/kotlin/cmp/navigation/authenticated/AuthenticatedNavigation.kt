/*
 * Copyright 2026 Mifos Initiative
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
import org.mifos.mobile.core.model.EventType
import org.mifos.mobile.core.model.StatusNavigationDestination
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.feature.accounts.accountTransactions.accountTransactionsDestination
import org.mifos.mobile.feature.accounts.accountTransactions.navigateToAccountTransactionsScreen
import org.mifos.mobile.feature.accounts.accounts.AccountNavRoute
import org.mifos.mobile.feature.accounts.accounts.accountsDestination
import org.mifos.mobile.feature.accounts.accounts.navigateToAccountsScreen
import org.mifos.mobile.feature.accounts.transactionDetail.navigateToTransactionDetails
import org.mifos.mobile.feature.accounts.transactionDetail.transactionDetailDestination
import org.mifos.mobile.feature.auth.login.navigateToLoginScreen
import org.mifos.mobile.feature.auth.navigation.AuthGraphRoute
import org.mifos.mobile.feature.beneficiary.beneficiaryApplication.navigateToManualBeneficiaryAddScreen
import org.mifos.mobile.feature.beneficiary.navigation.BeneficiaryNavRoute
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
import org.mifos.mobile.feature.recent.transaction.navigation.navigateToRecentTransactionScreen
import org.mifos.mobile.feature.recent.transaction.navigation.recentTransactionDestination
import org.mifos.mobile.feature.savings.application.navigation.navigateToSavingsApplicationGraph
import org.mifos.mobile.feature.savings.application.navigation.savingsApplicationNavGraph
import org.mifos.mobile.feature.savingsaccount.navigation.savingsNavGraph
import org.mifos.mobile.feature.savingsaccount.savingsAccountDetails.navigateToSavingsAccountDetailsScreen
import org.mifos.mobile.feature.settings.faq.faqDestination
import org.mifos.mobile.feature.settings.faq.navigateToFaq
import org.mifos.mobile.feature.settings.help.helpDestination
import org.mifos.mobile.feature.settings.help.navigateToHelp
import org.mifos.mobile.feature.share.application.navigation.navigateToShareApplicationGraph
import org.mifos.mobile.feature.share.application.navigation.shareApplicationNavGraph
import org.mifos.mobile.feature.shareaccount.navigation.shareNavGraph
import org.mifos.mobile.feature.shareaccount.shareAccountDetails.navigateToShareAccountDetailsScreen
import org.mifos.mobile.feature.status.navigation.StatusNavigationRoute
import org.mifos.mobile.feature.status.navigation.statusDestination
import org.mifos.mobile.feature.third.party.transfer.navigation.TptNavigationDestination
import org.mifos.mobile.feature.transfer.process.makeTransfer.makeTransferDestination
import org.mifos.mobile.feature.transfer.process.makeTransfer.navigateToMakeTransferScreen
import org.mifos.mobile.feature.transfer.process.transferProcess.navigateToTransferProcessScreen
import org.mifos.mobile.feature.transfer.process.transferProcess.transferProcessDestination

@Serializable
internal data object AuthenticatedGraphRoute

expect fun getPopRules(): Map<String, Int>

internal fun NavController.navigateToAuthenticatedGraph(navOptions: NavOptions? = null) {
    navigate(route = AuthenticatedGraphRoute, navOptions = navOptions)
}

@Suppress("CyclomaticComplexMethod", "LongMethod")
@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
internal fun NavGraphBuilder.authenticatedGraph(
    navController: NavController,
) {
    navigation<AuthenticatedGraphRoute>(
        startDestination = AuthenticatedNavbarRoute,
    ) {
        authenticatedNavbarGraph(
            homeNavigator = { destination ->
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

                    is HomeNavigationDestination.TransactionHistory ->
                        navController.navigateToRecentTransactionScreen()

                    is HomeNavigationDestination.ApplyLoan ->
                        navController.navigateToLoanApplicationGraph()

                    is HomeNavigationDestination.ApplySavings ->
                        navController.navigateToSavingsApplicationGraph()

                    is HomeNavigationDestination.ApplyShare ->
                        navController.navigateToShareApplicationGraph()
                }
            },

            tptNavigator = { destination ->
                when (destination) {
                    TptNavigationDestination.Notification -> navController.navigateToNotificationScreen()

                    is TptNavigationDestination.TransferProcess -> {
                        navController.navigateToTransferProcessScreen(
                            destination.payload,
                            TransferType.TPT,
                            StatusNavigationDestination.THIRD_PARTY_TRANSFER.name,
                        )
                    }

                    else -> {
                        navController.navigateToManualBeneficiaryAddScreen()
                    }
                }
            },
        )

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
                } else if (accountType == Constants.SHARE_ACCOUNTS) {
                    navController.navigateToShareAccountDetailsScreen(accountId)
                }
            },
        )

        accountTransactionsDestination(
            navigateBack = navController::popBackStack,
            navigateToDetails = { transactionId, accountType, accountId ->
                navController.navigateToTransactionDetails(
                    transactionId = transactionId,
                    accountType = accountType,
                    accountId = accountId,
                )
            },
        )

        transactionDetailDestination(
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

                    StatusNavigationDestination.SAVINGS_APPLICATION.name -> {
                        navController.navigateToAccountFromStatus(Constants.SAVINGS_ACCOUNT)
                    }

                    StatusNavigationDestination.LOAN_APPLICATION.name -> {
                        navController.navigateToAccountFromStatus(Constants.LOAN_ACCOUNT)
                    }

                    StatusNavigationDestination.SHARE_APPLICATION.name -> {
                        navController.navigateToAccountFromStatus(Constants.SHARE_ACCOUNTS)
                    }

                    StatusNavigationDestination.BENEFICIARY.name -> {
                        navController.navigateToBeneficiaryFromStatus()
                    }

                    StatusNavigationDestination.PREVIOUS_SCREEN.name -> {
                        navController.popScreens()
                    }

                    StatusNavigationDestination.THIRD_PARTY_TRANSFER.name -> {
                        navController.navigateToHomeAfterStatus()
                    }

                    StatusNavigationDestination.SAVINGS_ACCOUNT.name -> {
                        repeat(3) { navController.popBackStack() }
                    }

                    StatusNavigationDestination.LOAN_ACCOUNT.name -> {
                        repeat(3) { navController.popBackStack() }
                    }

                    StatusNavigationDestination.SAVINGS_UPDATE.name,
                    StatusNavigationDestination.SAVINGS_WITHDRAW.name,
                    -> {
                        repeat(2) { navController.popBackStack() }
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
            navigateToStatusScreen = navController::navigateToStatusScreenWithoutPopUpTo,
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

        shareNavGraph(
            navController = navController,
            navigateToClientChargeScreen = navController::navigateToClientChargeScreen,
            navigateToShareAccountTransactionScreen = { accountId ->
                navController.navigateToAccountTransactionsScreen(
                    Constants.SHARE_ACCOUNTS,
                    accountId,
                )
            },
            navigateToQrCodeScreen = navController::navigateToQrDisplayScreen,
        )

        loanApplicationNavGraph(
            navController = navController,
            navigateToAuthenticateScreen = navController::navigateToVerifyPasscodeScreen,
            navigateToStatusScreen = navController::navigateToStatusScreen,
        )

        savingsApplicationNavGraph(
            navController = navController,
            navigateToStatusScreen = navController::navigateToStatusScreen,
            navigateToAuthenticateScreen = navController::navigateToVerifyPasscodeScreen,
        )

        shareApplicationNavGraph(
            navController = navController,
            navigateToStatusScreen = navController::navigateToStatusScreen,
            navigateToAuthenticateScreen = navController::navigateToVerifyPasscodeScreen,
        )

        passcodeDestination(
            onPasscodeConfirm = navController::popBackStack,
        )

        locationsNavGraph()

        recentTransactionDestination(
            navigateBack = navController::popBackStack,
            navigateToDetails = { transactionId, accountType, accountId ->
                navController.navigateToTransactionDetails(
                    transactionId = transactionId,
                    accountType = accountType,
                    accountId = accountId,
                )
            },
        )

        beneficiaryNavGraph(
            navController = navController,
            navigateToQR = navController::navigateToQrReaderScreen,
            navigateToStatusScreen = navController::navigateToStatusScreen,
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
                    transferSuccessDestination = transferDestination,
                )
            },
        )

        transferProcessDestination(
            navigateBack = navController::popBackStack,
            navigateToAuthenticateScreen = navController::navigateToVerifyPasscodeScreen,
            navigateToStatusScreen = navController::navigateToStatusScreenWithoutPopUpTo,
        )

        faqDestination(
            onBackClick = navController::popBackStack,
            contact = navController::navigateToHelp,
        )

        helpDestination(
            onBackClick = navController::popBackStack,
            navigateToFAQ = navController::navigateToFaq,
        )
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

fun NavController.navigateToStatusScreen(
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
        if (eventType == EventType.SUCCESS.name) {
            popUpTo(AuthenticatedGraphRoute) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }
}

fun NavController.navigateToStatusScreenWithoutPopUpTo(
    eventType: String,
    eventDestination: String,
    title: String,
    subtitle: String,
    buttonText: String,
) {
    if (eventDestination == StatusNavigationDestination.THIRD_PARTY_TRANSFER.name) {
        this.navigate(
            StatusNavigationRoute(
                eventType = eventType,
                eventDestination = eventDestination,
                title = title,
                subtitle = subtitle,
                buttonText = buttonText,
            ),
        ) {
            if (eventType == EventType.SUCCESS.name) {
                popUpTo(AuthenticatedGraphRoute) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    } else {
        this.navigate(
            StatusNavigationRoute(
                eventType = eventType,
                eventDestination = eventDestination,
                title = title,
                subtitle = subtitle,
                buttonText = buttonText,
            ),
        ) {
            launchSingleTop = true
        }
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

fun NavController.navigateToAccountFromStatus(
    accountType: String,
) {
    this.navigate(AuthenticatedNavbarRoute) {
        popUpTo(StatusNavigationRoute::class) {
            inclusive = true
        }
        launchSingleTop = true
    }

    this.navigate(AccountNavRoute(accountType)) {
        launchSingleTop = true
    }
}

fun NavController.navigateToBeneficiaryFromStatus() {
    this.navigate(AuthenticatedNavbarRoute) {
        popUpTo(StatusNavigationRoute::class) {
            inclusive = true
        }
        launchSingleTop = true
    }

    this.navigate(BeneficiaryNavRoute) {
        launchSingleTop = true
    }
}

fun NavController.popScreens(
    popRules: Map<String, Int> = getPopRules(),
) {
    val lastEntry = previousBackStackEntry?.destination?.route

    val pops = popRules.entries
        .firstOrNull { (route, _) ->
            lastEntry?.startsWith(route) == true
        }
        ?.value ?: 1

    repeat(pops) { popBackStack() }
}
