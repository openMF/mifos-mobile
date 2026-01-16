/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.loanAccountSummary

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * A route for the account summary screen.
 *
 * @property accountId The ID of the loan account.
 */
@Serializable
data class AccountSummaryRoute(
    val accountId: Long,
)

/**
 * Navigates to the loan account summary screen.
 *
 * @param accountId The ID of the loan account.
 * @param navOptions The navigation options.
 */
fun NavController.navigateToLoanAccountSummaryScreen(
    accountId: Long,
    navOptions: NavOptions? = null,
) {
    navigate(AccountSummaryRoute(accountId), navOptions)
}

/**
 * Defines the loan account summary screen destination in the navigation graph.
 *
 * @param navigateBack A callback to navigate back to the previous screen.
 */
fun NavGraphBuilder.loanAccountSummaryDestination(
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<AccountSummaryRoute> {
        LoanAccountSummaryScreen(
            navigateBack = navigateBack,
        )
    }
}
