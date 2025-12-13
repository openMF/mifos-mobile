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

package org.mifos.mobile.feature.shareaccount.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.feature.shareaccount.shareAccount.ShareAccountRoute
import org.mifos.mobile.feature.shareaccount.shareAccount.shareAccountDestination
import org.mifos.mobile.feature.shareaccount.shareAccountDetails.navigateToShareAccountDetailsScreen
import org.mifos.mobile.feature.shareaccount.shareAccountDetails.shareAccountDetailsDestination

@Serializable
data object ShareGraphRoute
fun NavGraphBuilder.shareNavGraph(
    navController: NavController,
    navigateToClientChargeScreen: (String, Long) -> Unit,
    navigateToShareAccountTransactionScreen: (Long) -> Unit,
    navigateToQrCodeScreen: (String) -> Unit,
) {
    navigation<ShareGraphRoute>(
        startDestination = ShareAccountRoute,
    ) {
        shareAccountDestination(
            navigateBack = navController::popBackStack,
            onAccountClicked = { accountId ->
                navController.navigateToShareAccountDetailsScreen(accountId)
            },
        )

        shareAccountDetailsDestination(
            navigateBack = navController::popBackStack,

            navigateToClientChargeScreen = navigateToClientChargeScreen,
            navigateToShareAccountTransactionScreen = navigateToShareAccountTransactionScreen,
            navigateToQrCodeScreen = navigateToQrCodeScreen,
        )
    }
}
