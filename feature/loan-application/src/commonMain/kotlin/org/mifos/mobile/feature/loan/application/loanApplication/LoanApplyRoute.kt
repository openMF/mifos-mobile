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
 * Navigation route for the loan application screen.
 *
 * @param productId Selected loan product ID.
 * @param productName Selected loan product name.
 */
@Serializable
data class LoanApplyRoute(
    val productId: Int,
    val productName: String,
)

/**
 * Navigates to the loan application screen.
 *
 * @param productId Selected loan product ID.
 * @param productName Selected loan product name.
 * @param navOptions Optional navigation options.
 */
fun NavController.navigateToLoanApplyScreen(
    productId: Int,
    productName: String,
    navOptions: NavOptions? = null,
) =
    navigate(LoanApplyRoute(productId, productName), navOptions)

/**
 * Adds the loan application destination to the navigation graph.
 *
 * @param navigateBack Handles back navigation.
 * @param navigateToConfirmDetailsScreen Navigates to confirm details screen.
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
