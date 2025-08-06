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
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.feature.beneficiary.beneficiaryApplication.manualBeneficiaryAddDestination
import org.mifos.mobile.feature.beneficiary.beneficiaryApplication.navigateToManualBeneficiaryAddScreen
import org.mifos.mobile.feature.beneficiary.beneficiaryApplicationConfirmation.beneficiaryAddConfirmationDestination
import org.mifos.mobile.feature.beneficiary.beneficiaryApplicationConfirmation.navigateToBeneficiaryApplicationAddConfirmationScreen
import org.mifos.mobile.feature.beneficiary.beneficiaryDetail.beneficiaryDetailScreenDestination
import org.mifos.mobile.feature.beneficiary.beneficiaryDetail.navigateToBeneficiaryDetailScreen
import org.mifos.mobile.feature.beneficiary.beneficiaryList.BeneficiaryListNavRoute
import org.mifos.mobile.feature.beneficiary.beneficiaryList.beneficiaryListScreen

@Serializable
data object BeneficiaryNavRoute

fun NavController.navigateToBeneficiaryNavGraph(navOptions: NavOptions? = null) =
    navigate(BeneficiaryNavRoute, navOptions)

fun NavGraphBuilder.beneficiaryNavGraph(
    navController: NavController,
    navigateToQR: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
) {
    navigation<BeneficiaryNavRoute>(
        startDestination = BeneficiaryListNavRoute,
    ) {
        beneficiaryListScreen(
            navigateBack = navController::popBackStack,
            addBeneficiaryClicked = {
                navController.navigateToManualBeneficiaryAddScreen()
            },
            onBeneficiaryItemClick = {
                navController.navigateToBeneficiaryDetailScreen(it)
            },
        )

        manualBeneficiaryAddDestination(
            navigateToConfirmationScreen =
            navController::navigateToBeneficiaryApplicationAddConfirmationScreen,
            navigateBack = navController::popBackStack,
            navigateToQR = navigateToQR,
        )

        beneficiaryAddConfirmationDestination(
            navigateBack = navController::popBackStack,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
            navigateToStatusScreen = navigateToStatusScreen,
        )

        beneficiaryDetailScreenDestination(
            navigateBack = navController::popBackStack,
            updateBeneficiary = { beneficiaryId ->
                navController.navigateToManualBeneficiaryAddScreen(
                    beneficiaryId = beneficiaryId,
                    beneficiaryState = BeneficiaryState.UPDATE.name,
                )
            },
        )
    }
}
