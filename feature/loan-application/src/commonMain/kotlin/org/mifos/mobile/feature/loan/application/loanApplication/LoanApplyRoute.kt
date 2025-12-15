/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.loanApplication

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * Defines the navigation arguments required to launch the loan application screen.
 *
 * @param productId The unique identifier of the loan product.
 * @param productName The display name of the loan product.
 */
@Serializable
data class LoanApplyRoute(
    val productId: Int,
    val productName: String,
)

/**
 * Navigates to the loan application flow for a specific product.
 *
 * @param productId The unique identifier of the selected loan product.
 * @param productName The name of the product to display.
 */
fun NavController.navigateToLoanApplyScreen(
    productId: Int,
    productName: String,
    navOptions: NavOptions? = null,
) =
    navigate(LoanApplyRoute(productId, productName), navOptions)

/**
 * Registers the loan application screen in the navigation graph.
 *
 * @param navigateBack Callback to return to the previous screen.
 * @param navigateToConfirmDetailsScreen Callback to proceed to the confirmation screen with the application data.
 */
fun NavGraphBuilder.loanApplyDestination(
    navigateBack: () -> Unit,
    navigateToConfirmDetailsScreen: (Long, String, String, String, String, String) -> Unit,
) {
    composableWithSlideTransitions<LoanApplyRoute> {
        LoanApplyScreen(
            navigateBack = navigateBack,
            navigateToConfirmDetailsScreen = navigateToConfirmDetailsScreen,
        )
    }
}
