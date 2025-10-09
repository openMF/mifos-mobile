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

/**
 * Data class representing the navigation route for the beneficiary feature.
 *
 * @property BeneficiaryNavRoute the navigation route for the beneficiary feature.
 */
@Serializable
data object BeneficiaryNavRoute

/**
 * Navigate to the beneficiary navigation graph.
 *
 * @param navOptions the navigation options to use when navigating to the beneficiary navigation graph.
 */
fun NavController.navigateToBeneficiaryNavGraph(navOptions: NavOptions? = null) =
    navigate(BeneficiaryNavRoute, navOptions)

/**
 * Adds a navigation graph for the beneficiary feature.
 *
 * The navigation graph has the following destinations:
 * - Beneficiary List Screen: The screen that displays the list of all beneficiaries.
 * - Manual Beneficiary Add Screen: The screen that allows the user to manually add a beneficiary.
 * - Beneficiary Application Confirmation Screen: The screen that confirms the addition of a beneficiary.
 * - Beneficiary Detail Screen: The screen that displays the details of a beneficiary.
 *
 * The navigation graph is structured as follows:
 * - The Beneficiary List Screen is the starting point of the graph.
 * - The Manual Beneficiary Add Screen is reachable from the Beneficiary List Screen.
 * - The Beneficiary Application Confirmation Screen is reachable from the Manual Beneficiary Add Screen.
 * - The Beneficiary Detail Screen is reachable from the Beneficiary List Screen and the Manual Beneficiary Add Screen.
 *
 * @param navController The navigation controller to use for navigation.
 * @param navigateToQR A function to navigate to the QR code screen.
 * @param navigateToStatusScreen A function to navigate to the status screen.
 * @param navigateToAuthenticateScreen A function to navigate to the authentication screen.
 */
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
