/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.loanAccountRepaymentSchedule

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.entity.AccountDetails
import org.mifos.mobile.core.ui.composableWithSlideTransitions

@Serializable
data class RepaymentScheduleRoute(
    val accountId: Long,
)

fun NavController.navigateToLoanRepaymentScreen(
    accountId: Long,
    navOptions: NavOptions? = null,
) {
    navigate(RepaymentScheduleRoute(accountId), navOptions)
}

fun NavGraphBuilder.loanAccountRepaymentDestination(
    navigateToMakePaymentScreen: (AccountDetails) -> Unit,
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<RepaymentScheduleRoute> {
        ChargeDetailScreen(
            navigateBack = navigateBack,
            navigateToMakePaymentScreen = navigateToMakePaymentScreen,
        )
    }
}
