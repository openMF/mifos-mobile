/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.feature.loan.application.confirmDetails.confirmDetailsDestination
import org.mifos.mobile.feature.loan.application.confirmDetails.navigateToConfirmDetailsScreen
import org.mifos.mobile.feature.loan.application.loanApplication.loanApplyDestination
import org.mifos.mobile.feature.loan.application.loanApplication.navigateToLoanApplyScreen
import org.mifos.mobile.feature.loan.application.loanProductDescription.loanProductDetailsDestination
import org.mifos.mobile.feature.loan.application.loanProductDescription.navigateToLoanProductDetailsScreen
import org.mifos.mobile.feature.loan.application.loanType.SelectLoanTypeRoute
import org.mifos.mobile.feature.loan.application.loanType.selectLoanTypeDestination
import org.mifos.mobile.feature.loan.application.uploadDocs.uploadDocsDestination

/**
 * Root navigation graph for the loan application flow.
 */
@Serializable
data object LoanApplicationNavGraph

/**
 * Navigates to the loan application navigation graph.
 *
 * @param navOptions Optional navigation options.
 */
fun NavController.navigateToLoanApplicationGraph(navOptions: NavOptions? = null) {
    this.navigate(LoanApplicationNavGraph, navOptions)
}

/**
 * Defines the loan application navigation graph and destinations.
 *
 * @param navController NavController used for navigation actions.
 * @param navigateToAuthenticateScreen Navigates to authentication flow.
 * @param navigateToStatusScreen Navigates to loan status screen.
 */
fun NavGraphBuilder.loanApplicationNavGraph(
    navController: NavController,
    navigateToAuthenticateScreen: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
) {
    navigation<LoanApplicationNavGraph>(
        startDestination = SelectLoanTypeRoute,
    ) {
        selectLoanTypeDestination(
            navigateBack = navController::popBackStack,
            navigateToLoanProductDetailsScreen = { id, name ->
                navController.navigateToLoanProductDetailsScreen(id, name)
            },
        )

        loanApplyDestination(
            navigateBack = navController::popBackStack,
            navigateToConfirmDetailsScreen = navController::navigateToConfirmDetailsScreen,
        )

        confirmDetailsDestination(
            navigateBack = navController::popBackStack,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
            navigateToStatusScreen = navigateToStatusScreen,
        )

        loanProductDetailsDestination(
            navigateBack = navController::popBackStack,
            navigateToApplyLoanScreen = { productId, productName ->
                navController.navigateToLoanApplyScreen(productId ?: -1, productName)
            },
//            navigateToApplyLoanScreen = { _, _ ->
//                navController::navigateToUploadDocsScreen
//            }
        )

        uploadDocsDestination(
            navigateBack = navController::popBackStack,
            navigateToNext = {},
            navigateToPreviewDoc = {},
        )
    }
}
