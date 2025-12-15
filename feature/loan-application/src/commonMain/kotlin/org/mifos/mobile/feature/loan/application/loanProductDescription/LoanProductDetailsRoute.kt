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
 * Navigation route for the loan product details screen.
 *
 * @param productId Selected loan product ID.
 * @param productName Selected loan product name.
 */
@Serializable
data class LoanProductDetailsRoute(
    val productId: Int,
    val productName: String,
)

/**
 * Navigates to the loan product details screen.
 *
 * @param productId Selected loan product ID.
 * @param productName Selected loan product name.
 */
fun NavController.navigateToLoanProductDetailsScreen(
    productId: Int,
    productName: String,
) {
    this.navigate(LoanProductDetailsRoute(productId, productName))
}

/**
 * Adds the loan product details destination to the navigation graph.
 *
 * @param navigateBack Handles back navigation.
 * @param navigateToApplyLoanScreen Navigates to loan application screen.
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
