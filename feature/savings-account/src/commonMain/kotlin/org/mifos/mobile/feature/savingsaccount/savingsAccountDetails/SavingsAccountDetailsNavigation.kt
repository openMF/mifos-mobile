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

package org.mifos.mobile.feature.savingsaccount.savingsAccountDetails

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

@Serializable
data class SavingsAccountDetailsRoute(
    val accountId: Long,
)

fun NavController.navigateToSavingsAccountDetailsScreen(accountId: Long, navOptions: NavOptions? = null) =
    navigate(SavingsAccountDetailsRoute(accountId), navOptions)

fun NavGraphBuilder.savingsAccountDetailsDestination(
    navigateBack: () -> Unit,
    navigateToClientChargeScreen: (String, Long) -> Unit,
    navigateToUpdateScreen: (Long, String?, String?, String?, String?) -> Unit,
    navigateToSavingsAccountTransactionScreen: (Long) -> Unit,
    navigateToWithdrawScreen: (Long, String?, String?, String?, String?) -> Unit,
    navigateToQrCodeScreen: (String) -> Unit,
) {
    composableWithSlideTransitions<SavingsAccountDetailsRoute> {
        SavingsAccountDetailsScreen(
            navigateBack = navigateBack,
            navigateToClientChargeScreen = navigateToClientChargeScreen,
            navigateToUpdateScreen = navigateToUpdateScreen,
            navigateToWithdrawScreen = navigateToWithdrawScreen,
            navigateToSavingsAccountTransactionScreen = navigateToSavingsAccountTransactionScreen,
            navigateToQrCodeScreen = navigateToQrCodeScreen,
        )
    }
}
