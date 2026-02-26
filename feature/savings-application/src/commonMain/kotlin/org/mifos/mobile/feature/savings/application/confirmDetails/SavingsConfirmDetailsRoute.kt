/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.application.confirmDetails

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * Defines the type-safe route arguments required to display the savings confirmation details.
 *
 * @param savingsProductId The unique identifier for the selected savings product.
 * @param applicantName The name of the applicant.
 * @param savingsProductName The display name of the savings product.
 * @param currency The currency code for the savings account.
 * @param minOpeningBalance The minimum opening balance.
 * @param frequency The lock-in period frequency.
 * @param frequencyTypeName The name of the frequency type.
 * @param frequencyTypeId The ID of the frequency type.
 * @param allowOverdraft Whether overdraft is allowed.
 * @param submittedOnDate The submission date.
 */
@Serializable
data class SavingsConfirmDetailsRoute(
    val savingsProductId: Long,
    val applicantName: String,
    val savingsProductName: String,
    val currency: String,
    val minOpeningBalance: String,
    val frequency: String,
    val frequencyTypeName: String,
    val frequencyTypeId: Long,
    val allowOverdraft: Boolean,
    val submittedOnDate: String,
)

/**
 * Navigates to the Confirm Details screen with the specified application data.
 */
fun NavController.navigateToSavingsConfirmDetailsScreen(
    savingsProductId: Long,
    applicantName: String,
    savingsProductName: String,
    currency: String,
    minOpeningBalance: String,
    frequency: String,
    frequencyTypeName: String,
    frequencyTypeId: Long,
    allowOverdraft: Boolean,
    submittedOnDate: String,
    navOptions: NavOptions? = null,
) =
    navigate(
        SavingsConfirmDetailsRoute(
            savingsProductId,
            applicantName,
            savingsProductName,
            currency,
            minOpeningBalance,
            frequency,
            frequencyTypeName,
            frequencyTypeId,
            allowOverdraft,
            submittedOnDate,
        ),
        navOptions,
    )

/**
 * Registers the Confirm Details screen in the navigation graph.
 *
 * @param navigateToAuthenticateScreen Callback to proceed to authentication (e.g., PIN/Biometric).
 * @param navigateToStatusScreen Callback to show the final success/failure status after confirmation.
 * @param navigateBack Callback to return to the previous screen.
 */
fun NavGraphBuilder.savingsConfirmDetailsDestination(
    navigateToAuthenticateScreen: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<SavingsConfirmDetailsRoute> {
        SavingsConfirmDetailsScreen(
            navigateBack = navigateBack,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
            navigateToStatusScreen = navigateToStatusScreen,
        )
    }
}
