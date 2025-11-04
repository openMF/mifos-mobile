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

package org.mifos.mobile.feature.savingsaccount.savingsAccount

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * A type-safe, serializable route for the main Savings Account list screen.
 * This object serves as the unique identifier for this destination in the
 * navigation graph, making navigation more robust and less error-prone.
 */
@Serializable
data object SavingsAccountRoute

/**
 * Navigates to the main Savings Account list screen.
 *
 * This is an extension function on [NavController] that simplifies the process
 * of navigating to the savings account list destination.
 *
 * @param navOptions Optional [NavOptions] to apply to this navigation operation,
 *   allowing for customization of transition animations or back stack behavior.
 */
fun NavController.navigateToSavingsAccountScreen(navOptions: NavOptions? = null) =
    navigate(SavingsAccountRoute, navOptions)

/**
 * Defines the composable destination for the "Savings Account" list screen
 * within the navigation graph.
 *
 * This function sets up the route and the content to be displayed (`SavingsAccountScreen`),
 * along with specifying the screen enter/exit transitions.
 *
 * @param navigateBack A lambda function to be invoked when the user initiates a
 *   back action from the [SavingsAccountScreen].
 */
fun NavGraphBuilder.savingsAccountDestination(
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<SavingsAccountRoute> {
        SavingsAccountScreen(
            navigateBack = navigateBack,
        )
    }
}
