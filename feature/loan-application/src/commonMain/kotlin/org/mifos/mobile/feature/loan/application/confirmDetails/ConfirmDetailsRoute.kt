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
 * Navigation route for the confirm loan details screen.
 *
 * @param loanProductId Selected loan product ID.
 * @param applicantName Name of the applicant.
 * @param loanProductName Name of the loan product.
 * @param loanPurpose Purpose of the loan.
 * @param disbursementDate Selected disbursement date.
 * @param principalAmount Loan principal amount.
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
 * Navigates to the confirm details screen.
 *
 * @param loanProductId Selected loan product ID.
 * @param applicantName Name of the applicant.
 * @param loanProductName Name of the loan product.
 * @param loanPurpose Purpose of the loan.
 * @param disbursementDate Selected disbursement date.
 * @param principalAmount Loan principal amount.
 * @param navOptions Optional navigation options.
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
 * Adds the confirm details destination to the navigation graph.
 *
 * @param navigateToAuthenticateScreen Navigates to authentication screen.
 * @param navigateToStatusScreen Navigates to loan status screen.
 * @param navigateBack Handles back navigation.
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
