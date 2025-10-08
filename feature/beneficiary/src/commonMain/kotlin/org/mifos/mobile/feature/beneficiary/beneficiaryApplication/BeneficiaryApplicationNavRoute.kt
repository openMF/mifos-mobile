/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryApplication

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.composableWithSlideTransitions
/**
 * Data class representing the navigation route for the beneficiary application screen.
 *
 * @property beneficiaryId the ID of the beneficiary.
 * @property beneficiaryState the state of the beneficiary.
 * @property name the name of the beneficiary.
 * @property accountType the account type of the beneficiary.
 * @property accountNumber the account number of the beneficiary.
 * @property officeName the office name of the beneficiary.
 */
@Serializable
data class BeneficiaryApplicationNavRoute(
    val beneficiaryId: Long = -1L,
    val beneficiaryState: String = BeneficiaryState.CREATE_MANUAL.name,
    val name: String = "",
    val accountType: Int = -1,
    val accountNumber: String = "",
    val officeName: String = "",
)

/**
 * Navigate to the manual beneficiary add screen.
 *
 * @param beneficiaryId the ID of the beneficiary.
 * @param name the name of the beneficiary.
 * @param accountType the account type of the beneficiary.
 * @param accountNumber the account number of the beneficiary.
 * @param officeName the office name of the beneficiary.
 * @param beneficiaryState the state of the beneficiary.
 * @param navOptions the navigation options.
 */
fun NavController.navigateToManualBeneficiaryAddScreen(
    beneficiaryId: Long = -1L,
    name: String = "",
    accountType: Int = -1,
    accountNumber: String = "",
    officeName: String = "",
    beneficiaryState: String = BeneficiaryState.CREATE_MANUAL.name,
    navOptions: NavOptions? = null,
) {
    this.navigate(
        BeneficiaryApplicationNavRoute(
            beneficiaryId = beneficiaryId,
            name = name,
            accountType = accountType,
            accountNumber = accountNumber,
            officeName = officeName,
            beneficiaryState = beneficiaryState,
        ),
        navOptions,
    )
}

/**
 * Composable function to define the manual beneficiary add destination in the navigation graph.
 *
 * @param navigateBack a function to navigate back to the previous screen.
 * @param navigateToQR a function to navigate to the QR code screen.
 * @param navigateToConfirmationScreen a function to navigate to the confirmation screen.
 */
fun NavGraphBuilder.manualBeneficiaryAddDestination(
    navigateBack: () -> Unit,
    navigateToQR: () -> Unit,
    navigateToConfirmationScreen: (
        beneficiaryId: Long,
        beneficiaryState: String,
        name: String,
        officeName: String,
        accountType: Int,
        accountNumber: String,
        transferLimit: Int,
    ) -> Unit,
) {
    composableWithSlideTransitions<BeneficiaryApplicationNavRoute> {
        BeneficiaryApplicationScreen(
            navigateBack = navigateBack,
            navigateToConfirmationScreen = navigateToConfirmationScreen,
            navigateToQR = navigateToQR,
        )
    }
}
