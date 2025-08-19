/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.application.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.feature.savings.application.fillApplication.navigateToSavingsFillApplicationScreen
import org.mifos.mobile.feature.savings.application.fillApplication.savingsFillApplicationDestination
import org.mifos.mobile.feature.savings.application.savingsApplication.SavingsApplyRoute
import org.mifos.mobile.feature.savings.application.savingsApplication.savingsApplyDestination

@Serializable
data object SavingsApplicationNavGraph

fun NavController.navigateToSavingsApplicationGraph(navOptions: NavOptions? = null) {
    this.navigate(SavingsApplicationNavGraph, navOptions)
}

fun NavGraphBuilder.savingsApplicationNavGraph(
    navController: NavController,
    navigateToAuthenticateScreen: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
) {
    navigation<SavingsApplicationNavGraph>(
        startDestination = SavingsApplyRoute,
    ) {
        savingsApplyDestination(
            navigateBack = navController::popBackStack,
            navigateToFillDetailsScreen = navController::navigateToSavingsFillApplicationScreen,
        )

        savingsFillApplicationDestination(
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
            navigateToStatusScreen = navigateToStatusScreen,
            navigateBack = navController::popBackStack,
        )
    }
}
