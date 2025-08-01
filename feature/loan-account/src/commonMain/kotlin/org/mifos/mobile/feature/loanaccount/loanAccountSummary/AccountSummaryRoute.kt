/*
 * Copyright 2025 Mifos Initiative
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

@Serializable
data class AccountSummaryRoute(
    val accountId: Long,
)

fun NavController.navigateToLoanAccountSummaryScreen(
    accountId: Long,
    navOptions: NavOptions? = null,
) {
    navigate(AccountSummaryRoute(accountId), navOptions)
}

fun NavGraphBuilder.loanAccountSummaryDestination(
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<AccountSummaryRoute> {
        LoanAccountSummaryScreen(
            navigateBack = navigateBack,
        )
    }
}
