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
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.feature.accounts.navigation.accountTransactionsDestination
import org.mifos.mobile.feature.accounts.navigation.accountsDestination
import org.mifos.mobile.feature.accounts.navigation.navigateToAccountsScreen
import org.mifos.mobile.feature.auth.login.navigateToLoginScreen
import org.mifos.mobile.feature.auth.navigation.AuthGraphRoute
import org.mifos.mobile.feature.charge.navigation.clientChargeNavGraph
import org.mifos.mobile.feature.notification.navigation.navigateToNotificationScreen
import org.mifos.mobile.feature.notification.navigation.notificationDestination
import org.mifos.mobile.feature.passcode.navigation.PasscodeRoute
import org.mifos.mobile.feature.passcode.verifyPasscode.navigateToVerifyPasscodeScreen
import org.mifos.mobile.feature.passcode.verifyPasscode.passcodeDestination
import org.mifos.mobile.feature.savingsaccount.navigation.savingsNavGraph
import org.mifos.mobile.feature.savingsaccount.savingsAccountDetails.navigateToSavingsAccountDetailsScreen
import org.mifos.mobile.feature.status.navigation.StatusNavigationRoute
import org.mifos.mobile.feature.status.navigation.statusDestination

@Serializable
internal data object AuthenticatedGraphRoute

internal fun NavController.navigateToAuthenticatedGraph(navOptions: NavOptions? = null) {
    navigate(route = AuthenticatedGraphRoute, navOptions = navOptions)
}

internal fun NavGraphBuilder.authenticatedGraph(
    navController: NavController,
) {
    navigation<AuthenticatedGraphRoute>(
        startDestination = AuthenticatedNavbarRoute,
    ) {
        authenticatedNavbarGraph(
            navigateToNotificationScreen = navController::navigateToNotificationScreen,
            navigateToAccountsScreen = {
                when {
                    it == Constants.SAVINGS_ACCOUNT -> navController.navigateToAccountsScreen(it)
                    else -> Unit
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
                }
            },
        )

        accountTransactionsDestination(
            navigateBack = navController::popBackStack,
        )

        clientChargeNavGraph(
            navigateBack = navController::popBackStack,
            navController = navController,
        )

        statusDestination(
            navigateToDestination = {
                if (it == Constants.LOGIN) {
                    navController.navigateToLoginScreen()
                } else {
                    navController.navigateToHomeAfterStatus()
                }
            },
        )

        savingsNavGraph(
            navController = navController,
            navigateToStatusScreen = navController::navigateToStatusAfterUpdate,
            navigateToAuthenticateScreen = navController::navigateToVerifyPasscodeScreen,
        )

        passcodeDestination(
            onPasscodeConfirm = navController::popBackStack,
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
