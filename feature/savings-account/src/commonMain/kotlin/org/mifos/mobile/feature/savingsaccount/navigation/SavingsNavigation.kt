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

package org.mifos.mobile.feature.savingsaccount.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.feature.savingsaccount.savingsAccount.SavingsAccountRoute
import org.mifos.mobile.feature.savingsaccount.savingsAccount.savingsAccountDestination
import org.mifos.mobile.feature.savingsaccount.savingsAccountDetails.savingsAccountDetailsDestination

@Serializable
data object SavingsGraphRoute

fun NavController.navigateToSavingsGraph(navOptions: NavOptions? = null) =
    navigate(SavingsAccountRoute, navOptions)

fun NavGraphBuilder.savingsNavGraph(
    navController: NavController,
) {
    navigation<SavingsGraphRoute>(
        startDestination = SavingsAccountRoute,
    ) {
        savingsAccountDestination(
            navigateBack = navController::popBackStack,
        )

        savingsAccountDetailsDestination(
            navigateBack = navController::popBackStack,
        )
    }
}
