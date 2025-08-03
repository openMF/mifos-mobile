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

@Serializable
data class ConfirmDetailsRoute(
    val loanProductId: Long,
    val applicantName: String,
    val loanProductName: String,
    val loanPurpose: String,
    val disbursementDate: String,
    val principalAmount: String,
)

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
