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

package org.mifos.mobile.feature.beneficiary.beneficiaryApplicationConfirmation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

@Serializable
data class BeneficiaryApplicationConfirmationNavRoute(
    val beneficiaryId: Long = -1L,
    val beneficiaryState: String,
    val name: String,
    val officeName: String,
    val accountType: Int,
    val accountNumber: String,
    val transferLimit: Int,
)

fun NavController.navigateToBeneficiaryApplicationAddConfirmationScreen(
    beneficiaryId: Long = -1L,
    beneficiaryState: String,
    name: String,
    officeName: String,
    accountType: Int,
    accountNumber: String,
    transferLimit: Int,
    navOptions: NavOptions? = null,
) {
    this.navigate(
        BeneficiaryApplicationConfirmationNavRoute(
            beneficiaryId,
            beneficiaryState,
            name,
            officeName,
            accountType,
            accountNumber,
            transferLimit,
        ),
        navOptions,
    )
}

fun NavGraphBuilder.beneficiaryAddConfirmationDestination(
    navigateBack: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
) {
    composableWithSlideTransitions<BeneficiaryApplicationConfirmationNavRoute> {
        BeneficiaryApplicationConfirmationScreen(
            navigateBack = navigateBack,
            navigateToStatusScreen = navigateToStatusScreen,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
        )
    }
}
