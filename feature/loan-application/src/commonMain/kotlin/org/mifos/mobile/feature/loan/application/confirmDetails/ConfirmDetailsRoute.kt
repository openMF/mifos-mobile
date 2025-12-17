/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.confirmDetails

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * Defines the type-safe route arguments required to display the loan confirmation details.
 *
 * @param loanProductId The unique identifier for the selected loan product.
 * @param applicantName The name of the applicant.
 * @param loanProductName The display name of the loan product.
 * @param loanPurpose The stated purpose for the loan.
 * @param disbursementDate The scheduled date for funds disbursement.
 * @param principalAmount The requested loan amount.
 */
@Serializable
data class ConfirmDetailsRoute(
    val loanProductId: Long,
    val applicantName: String,
    val loanProductName: String,
    val loanPurpose: String,
    val disbursementDate: String,
    val principalAmount: String,
)

/**
 * Navigates to the Confirm Details screen with the specified application data.
 *
 * @param loanProductId The unique identifier for the selected loan product.
 * @param applicantName The name of the applicant.
 * @param loanProductName The display name of the loan product.
 * @param loanPurpose The stated purpose for the loan.
 * @param disbursementDate The scheduled date for funds disbursement.
 * @param principalAmount The requested loan amount.
 */
fun NavController.navigateToConfirmDetailsScreen(
    loanProductId: Long,
    applicantName: String,
    loanProductName: String,
    loanPurpose: String,
    disbursementDate: String,
    principalAmount: String,
    navOptions: NavOptions? = null,
) =
    navigate(
        ConfirmDetailsRoute(
            loanProductId,
            applicantName,
            loanProductName,
            loanPurpose,
            disbursementDate,
            principalAmount,
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
fun NavGraphBuilder.confirmDetailsDestination(
    navigateToAuthenticateScreen: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<ConfirmDetailsRoute> {
        ConfirmDetailsScreen(
            navigateBack = navigateBack,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
            navigateToStatusScreen = navigateToStatusScreen,
        )
    }
}
