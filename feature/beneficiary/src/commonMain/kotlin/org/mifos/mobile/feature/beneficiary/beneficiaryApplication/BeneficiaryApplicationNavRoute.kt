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
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.composableWithSlideTransitions

@Serializable
data class BeneficiaryApplicationNavRoute(
    val beneficiaryId: Int = -1,
    val beneficiaryState: String,
)

fun NavController.navigateToManualBeneficiaryAddScreen(
    beneficiaryId: Int,
    beneficiaryState: String,
    navOptions: NavOptions? = null,
) {
    this.navigate(BeneficiaryApplicationNavRoute(beneficiaryId, beneficiaryState), navOptions)
}

fun NavGraphBuilder.manualBeneficiaryAddDestination(
    navigateBack: () -> Unit,
    navigateToConfirmationScreen: (beneficiaryId: Int, beneficiary: BeneficiaryPayload, beneficiaryState: BeneficiaryState) -> Unit,
) {
    composableWithSlideTransitions<BeneficiaryApplicationNavRoute> {
        BeneficiaryApplicationScreen(
            navigateBack = navigateBack,
            navigateToConfirmationScreen = navigateToConfirmationScreen,
        )
    }
}
