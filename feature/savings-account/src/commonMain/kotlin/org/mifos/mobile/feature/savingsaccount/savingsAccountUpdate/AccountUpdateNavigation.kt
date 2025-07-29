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

package org.mifos.mobile.feature.savingsaccount.savingsAccountUpdate

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

@Serializable
data class SavingsAccountUpdateRoute(
    val accountId: Long,
    val clientName: String? = null,
    val submissionData: String? = null,
    val accountNumber: String? = null,
    val product: String? = null,
)

fun NavController.navigateToSavingsAccountUpdateScreen(
    accountId: Long,
    clientName: String?,
    submissionData: String?,
    accountNumber: String?,
    product: String?,
    navOptions: NavOptions? = null,
) =
    navigate(
        SavingsAccountUpdateRoute(
            accountId,
            clientName,
            submissionData,
            accountNumber,
            product,
        ),
        navOptions,
    )

fun NavGraphBuilder.savingsAccountUpdateDestination(
    navigateBack: () -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
) {
    composableWithSlideTransitions<SavingsAccountUpdateRoute> {
        AccountUpdateScreen(
            navigateBack = navigateBack,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
            navigateToStatusScreen = navigateToStatusScreen,
        )
    }
}
