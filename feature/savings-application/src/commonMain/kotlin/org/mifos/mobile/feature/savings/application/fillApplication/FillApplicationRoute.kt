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

package org.mifos.mobile.feature.savings.application.fillApplication

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * A type-safe, serializable route for the "Fill Savings Application" screen.
 *
 * This class encapsulates the necessary parameters required to fill out a new
 * savings account application, ensuring robust and error-free navigation.
 *
 * @property savingsProductId The unique identifier of the selected savings product.
 * @property savingsProductName The name of the selected savings product.
 */
@Serializable
data class SavingsFillApplicationRoute(
    val savingsProductId: Long,
    val savingsProductName: String,
)

/**
 * Navigates to the "Fill Savings Application" screen.
 *
 * This is an extension function on [NavController] that simplifies the process
 * of navigating to the application form by constructing and passing the
 * [SavingsFillApplicationRoute] with the required product details.
 *
 * @param savingsProductId The ID of the savings product.
 * @param savingsProductName The name of the savings product.
 * @param navOptions Optional [NavOptions] to apply to this navigation operation.
 */
fun NavController.navigateToSavingsFillApplicationScreen(
    savingsProductId: Long,
    savingsProductName: String,
    navOptions: NavOptions? = null,
) {
    this.navigate(SavingsFillApplicationRoute(savingsProductId, savingsProductName), navOptions)
}

/**
 * Defines the composable destination for the "Fill Savings Application" screen
 * within the navigation graph.
 *
 * This function sets up the route and the screen content (`SavingsFillApplicationScreen`),
 * and wires up the necessary navigation callbacks for actions initiated from the screen.
 *
 * @param navigateToConfirmDetailsScreen A lambda to navigate to the confirmation preview screen,
 *   where users can review their application before final submission.
 * @param navigateBack A lambda function to handle the back navigation event.
 */
@Suppress("MaxLineLength")
fun NavGraphBuilder.savingsFillApplicationDestination(
    navigateToConfirmDetailsScreen: (Long, String, String, String, String, String, String, Long, Boolean, String) -> Unit,
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<SavingsFillApplicationRoute> {
        SavingsFillApplicationScreen(
            navigateBack = navigateBack,
            navigateToConfirmDetailsScreen = navigateToConfirmDetailsScreen,
        )
    }
}
