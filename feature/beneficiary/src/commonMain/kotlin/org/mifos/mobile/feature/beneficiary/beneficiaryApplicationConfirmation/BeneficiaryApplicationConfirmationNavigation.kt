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

/**
 * Data class representing the navigation route for the beneficiary application confirmation screen.
 *
 * @property beneficiaryId the ID of the beneficiary.
 * @property beneficiaryState the state of the beneficiary.
 * @property name the name of the beneficiary.
 * @property officeName the office name of the beneficiary.
 * @property accountType the account type of the beneficiary.
 * @property accountNumber the account number of the beneficiary.
 * @property transferLimit the transfer limit of the beneficiary.
 */
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

/**
 * Navigates to the beneficiary application confirmation screen.
 *
 * @param beneficiaryId the ID of the beneficiary.
 * @param beneficiaryState the state of the beneficiary.
 * @param name the name of the beneficiary.
 * @param officeName the office name of the beneficiary.
 * @param accountType the account type of the beneficiary.
 * @param accountNumber the account number of the beneficiary.
 * @param transferLimit the transfer limit of the beneficiary.
 * @param navOptions the navigation options.
 */
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

/**
 * Adds a destination to the navigation graph for the beneficiary application confirmation screen.
 *
 * @param navigateBack a function to navigate back to the previous screen.
 * @param navigateToStatusScreen a function to navigate to the status screen.
 * @param navigateToAuthenticateScreen a function to navigate to the authentication screen.
 */
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
