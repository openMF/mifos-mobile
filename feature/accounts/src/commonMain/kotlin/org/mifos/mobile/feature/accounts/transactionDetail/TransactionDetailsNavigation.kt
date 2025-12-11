/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:Suppress("MatchingDeclarationName")

package org.mifos.mobile.feature.accounts.transactionDetail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

@Serializable
data class TransactionDetailsNavRoute(
    val transactionId: String,
    val accountType: String,
    val accountId: Long,
)

fun NavController.navigateToTransactionDetails(
    transactionId: String,
    accountType: String,
    accountId: Long,
    navOptions: NavOptions? = null,
) {
    this.navigate(TransactionDetailsNavRoute(transactionId, accountType, accountId), navOptions)
}

fun NavGraphBuilder.transactionDetailDestination(
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<TransactionDetailsNavRoute> {
        TransactionDetailsScreen(navigateBack)
    }
}
