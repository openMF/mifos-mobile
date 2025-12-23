/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.loanProductDescription

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * Defines the navigation arguments required to display the details of a specific loan product.
 *
 * @param productId The unique identifier of the loan product.
 * @param productName The display name of the loan product.
 */
@Serializable
data class LoanProductDetailsRoute(
    val productId: Int,
    val productName: String,
)

/**
 * Navigates to the product details screen for the selected loan.
 *
 * @param productId The unique identifier of the loan product.
 * @param productName The name of the product to display.
 */
fun NavController.navigateToLoanProductDetailsScreen(
    productId: Int,
    productName: String,
) {
    this.navigate(LoanProductDetailsRoute(productId, productName))
}

/**
 * Registers the loan product details screen in the navigation graph.
 *
 * @param navigateBack Callback to return to the previous screen.
 * @param navigateToApplyLoanScreen Callback to proceed to the application form for this product.
 */
fun NavGraphBuilder.loanProductDetailsDestination(
    navigateBack: () -> Unit,
    navigateToApplyLoanScreen: (productId: Int?, productName: String) -> Unit,
) {
    composableWithSlideTransitions<LoanProductDetailsRoute> {
        LoanProductDetailsScreen(
            navigateBack = navigateBack,
            navigateToApplyLoanScreen = navigateToApplyLoanScreen,
        )
    }
}
