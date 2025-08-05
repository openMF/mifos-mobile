/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.feature.beneficiary.beneficiaryList.BeneficiaryListNavRoute
import org.mifos.mobile.feature.beneficiary.beneficiaryList.beneficiaryListScreen

@Serializable
data object BeneficiaryNavRoute

fun NavController.navigateToBeneficiaryNavGraph(navOptions: NavOptions? = null) =
    navigate(BeneficiaryNavRoute, navOptions)

fun NavGraphBuilder.beneficiaryNavGraph(
    navController: NavController,
) {
    navigation<BeneficiaryNavRoute>(
        startDestination = BeneficiaryListNavRoute,
    ) {
        beneficiaryListScreen(
            navigateBack = navController::popBackStack,
            addBeneficiaryClicked = {},
            onBeneficiaryItemClick = {},
        )
    }
}
