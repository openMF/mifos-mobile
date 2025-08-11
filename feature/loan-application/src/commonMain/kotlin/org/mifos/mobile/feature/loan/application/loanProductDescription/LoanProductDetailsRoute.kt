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

@Serializable
data class LoanProductDetailsRoute(
    val productId: Int,
    val productName: String,
)

fun NavController.navigateToLoanProductDetailsScreen(
    productId: Int,
    productName: String,
) {
    this.navigate(LoanProductDetailsRoute(productId, productName))
}

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
