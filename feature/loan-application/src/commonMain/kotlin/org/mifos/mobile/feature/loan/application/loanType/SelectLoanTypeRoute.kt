/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.loanType

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions
import org.mifos.mobile.feature.loan.application.loanProductDescription.navigateToLoanProductDetailsScreen

/**
 * Navigation route for selecting a loan type.
 */
@Serializable
data object SelectLoanTypeRoute

/**
 * Adds the select loan type destination to the navigation graph.
 *
 * @param navigateBack Handles back navigation.
 * @param navigateToLoanProductDetailsScreen Navigates to loan product details screen.
 */
fun NavGraphBuilder.selectLoanTypeDestination(
    navigateBack: () -> Unit,
    navigateToLoanProductDetailsScreen: (Int, String) -> Unit,
) {
    composableWithSlideTransitions<SelectLoanTypeRoute> {
        SelectLoanTypeScreen(
            navigateBack = navigateBack,
            navigateToLoanProductDetailsScreen = navigateToLoanProductDetailsScreen,
        )
    }
}

/**
 * Navigates to the select loan type screen.
 *
 * @param navOptions Optional navigation options.
 */
fun NavController.navigateToSelectLoanType(navOptions: NavOptions? = null) =
    navigate(SelectLoanTypeRoute, navOptions)
