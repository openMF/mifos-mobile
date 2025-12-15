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

/**
 * A type-safe, serializable object representing the route for the nested
 * Savings Application navigation graph.
 */
@Serializable
data object SavingsApplicationNavGraph

/**
 * Navigates to the Savings Application navigation graph.
 *
 * This is a convenience extension function on [NavController] that encapsulates
 * the logic for navigating to the start of the savings application feature.
 *
 * @param navOptions Optional [NavOptions] to apply to this navigation operation.
 */
fun NavController.navigateToSavingsApplicationGraph(navOptions: NavOptions? = null) {
    this.navigate(SavingsApplicationNavGraph, navOptions)
}

/**
 * Builds the nested navigation graph for the savings account application feature.
 *
 * This function defines all the destinations within the savings application module
 * (product selection and form filling) and wires them together. It promotes a
 * decoupled architecture by accepting lambdas for navigation to external screens.
 *
 * @param navController The [NavController] used for handling navigation events within the graph.
 * @param navigateToAuthenticateScreen Lambda to navigate to an authentication screen,
 *   required before submitting the application.
 * @param navigateToStatusScreen Lambda to navigate to a generic status/result screen
 *   after the application submission is complete.
 */
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
