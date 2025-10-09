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

package org.mifos.mobile.feature.beneficiary.beneficiaryList

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithPushTransitions

@Serializable
data object BeneficiaryListNavRoute

/**
 * Navigates to the beneficiary list screen.
 */
fun NavController.navigateToBeneficiaryListScreen() {
    this.navigate(BeneficiaryListNavRoute)
}

/**
 * Adds a destination to the navigation graph for the beneficiary list screen.
 *
 * @param navigateBack a function to navigate back to the previous screen.
 * @param addBeneficiaryClicked a function to navigate to the add beneficiary screen.
 * @param onBeneficiaryItemClick a function to navigate to a beneficiary detail screen when a
 * beneficiary item is clicked.
 */
fun NavGraphBuilder.beneficiaryListScreen(
    navigateBack: () -> Unit,
    addBeneficiaryClicked: () -> Unit,
    onBeneficiaryItemClick: (position: Long) -> Unit,
) {
    composableWithPushTransitions<BeneficiaryListNavRoute> {
        BeneficiaryListScreen(
            navigateBack = navigateBack,
            addBeneficiaryClicked = addBeneficiaryClicked,
            onBeneficiaryItemClick = onBeneficiaryItemClick,
        )
    }
}
