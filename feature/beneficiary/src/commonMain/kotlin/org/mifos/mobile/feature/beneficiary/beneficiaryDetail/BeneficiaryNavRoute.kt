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

package org.mifos.mobile.feature.beneficiary.beneficiaryDetail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

/**
 * Represents the navigation route for the Beneficiary Detail screen.
 *
 * @property beneficiaryId The ID of the beneficiary to display.
 */

@Serializable
data class BeneficiaryDetailNavRoute(
    val beneficiaryId: Long = -1L,
)

/**
 * Navigates to the Beneficiary Detail screen.
 *
 * @param beneficiaryId The ID of the beneficiary to display.
 * @param navOptions The navigation options to use for navigation.
 */
fun NavController.navigateToBeneficiaryDetailScreen(
    beneficiaryId: Long = -1L,
    navOptions: NavOptions? = null,
) {
    this.navigate(BeneficiaryDetailNavRoute(beneficiaryId), navOptions)
}

/**
 * Adds a destination to the navigation graph for the Beneficiary Detail screen.
 *
 * @param navigateBack a function to navigate back to the previous screen.
 * @param updateBeneficiary a function to update a beneficiary with the given ID.
 */
fun NavGraphBuilder.beneficiaryDetailScreenDestination(
    navigateBack: () -> Unit,
    updateBeneficiary: (beneficiaryId: Long) -> Unit,
) {
    composableWithSlideTransitions<BeneficiaryDetailNavRoute> {
        BeneficiaryDetailScreen(
            navigateBack = navigateBack,
            updateBeneficiary = updateBeneficiary,
        )
    }
}
