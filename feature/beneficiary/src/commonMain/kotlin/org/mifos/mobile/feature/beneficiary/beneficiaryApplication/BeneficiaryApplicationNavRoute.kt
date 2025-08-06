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

@Serializable
data class BeneficiaryApplicationNavRoute(
    val beneficiaryId: Long = -1L,
    val beneficiaryState: String = BeneficiaryState.CREATE_MANUAL.name,
    val name: String = "",
    val accountType: Int = -1,
    val accountNumber: String = "",
    val officeName: String = "",
)

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
