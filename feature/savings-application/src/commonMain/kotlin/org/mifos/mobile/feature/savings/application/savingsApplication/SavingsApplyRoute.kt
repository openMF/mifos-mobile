/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.application.savingsApplication

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * A type-safe, serializable object representing the route for the initial
 * "Apply for Savings Account" screen. This serves as the entry point for
 * the savings application flow.
 */
@Serializable
data object SavingsApplyRoute

/**
 * Navigates to the "Apply for Savings Account" screen.
 *
 * This is an extension function on [NavController] that simplifies navigating
 * to the savings product selection screen.
 *
 * @param navOptions Optional [NavOptions] to apply to this navigation operation.
 */
fun NavController.navigateToSavingsApplyScreen(
    navOptions: NavOptions? = null,
) =
    navigate(SavingsApplyRoute, navOptions)

/**
 * Defines the composable destination for the "Apply for Savings Account" screen
 * within the navigation graph.
 *
 * This function sets up the route, the screen content (`SavingsApplyScreen`),
 * and wires up the navigation callbacks for actions initiated from this screen.
 *
 * @param navigateToFillDetailsScreen A lambda to navigate to the application form screen,
 *   passing the selected product ID and product name.
 * @param navigateBack A lambda function to handle the back navigation event.
 */
fun NavGraphBuilder.savingsApplyDestination(
    navigateToFillDetailsScreen: (Long, String) -> Unit,
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<SavingsApplyRoute> {
        SavingsApplyScreen(
            navigateBack = navigateBack,
            navigateToFillDetailsScreen = navigateToFillDetailsScreen,
        )
    }
}
