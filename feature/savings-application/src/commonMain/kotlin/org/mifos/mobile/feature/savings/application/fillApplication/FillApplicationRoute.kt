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
 * @property fieldOfficerId The unique identifier of the assigned field officer.
 * @property fieldOfficerName The name of the assigned field officer.
 */
@Serializable
data class SavingsFillApplicationRoute(
    val savingsProductId: Long,
    val fieldOfficerId: Long,
    val fieldOfficerName: String,
)

/**
 * Navigates to the "Fill Savings Application" screen.
 *
 * This is an extension function on [NavController] that simplifies the process
 * of navigating to the application form by constructing and passing the
 * [SavingsFillApplicationRoute] with the required product and officer details.
 *
 * @param savingsProductId The ID of the savings product.
 * @param fieldOfficerId The ID of the field officer.
 * @param fieldOfficerName The name of the field officer.
 * @param navOptions Optional [NavOptions] to apply to this navigation operation.
 */
fun NavController.navigateToSavingsFillApplicationScreen(
    savingsProductId: Long,
    fieldOfficerId: Long,
    fieldOfficerName: String,
    navOptions: NavOptions? = null,
) {
    this.navigate(SavingsFillApplicationRoute(savingsProductId, fieldOfficerId, fieldOfficerName), navOptions)
}

/**
 * Defines the composable destination for the "Fill Savings Application" screen
 * within the navigation graph.
 *
 * This function sets up the route and the screen content (`SavingsFillApplicationScreen`),
 * and wires up the necessary navigation callbacks for actions initiated from the screen.
 *
 * @param navigateToAuthenticateScreen A lambda to navigate to an authentication screen,
 *   typically required before submitting the application.
 * @param navigateToStatusScreen A lambda to navigate to a generic status/result screen
 *   after the application submission is complete.
 * @param navigateBack A lambda function to handle the back navigation event.
 */
fun NavGraphBuilder.savingsFillApplicationDestination(
    navigateToAuthenticateScreen: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<SavingsFillApplicationRoute> {
        SavingsFillApplicationScreen(
            navigateBack = navigateBack,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
            navigateToStatusScreen = navigateToStatusScreen,
        )
    }
}
