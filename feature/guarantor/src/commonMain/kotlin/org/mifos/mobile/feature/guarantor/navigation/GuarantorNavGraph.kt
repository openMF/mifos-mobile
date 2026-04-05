/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.guarantor.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import org.mifos.mobile.core.common.Constants.INDEX
import org.mifos.mobile.core.common.Constants.LOAN_ID
import org.mifos.mobile.feature.guarantor.screens.guarantorAdd.AddGuarantorScreen
import org.mifos.mobile.feature.guarantor.screens.guarantorDetails.GuarantorDetailScreen
import org.mifos.mobile.feature.guarantor.screens.guarantorList.GuarantorListScreen

/**
 * Navigates to the Guarantor screen.
 *
 * This is an extension function on [NavController] that simplifies navigating
 * to the guarantor screen with the specified loan ID.
 *
 * @param loanId The ID of the loan for which guarantors are being managed.
 */
fun NavController.navigateToGuarantorScreen(loanId: Long) {
    navigate(GuarantorNavigation.GuarantorScreenBase.passArguments(loanId = loanId.toString()))
}

/**
 * Navigates to the Guarantor List screen.
 *
 * This is an extension function on [NavController] that simplifies navigating
 * to the guarantor list screen with the specified loan ID.
 *
 * @param loanId The ID of the loan for which guarantors are being displayed.
 */
fun NavController.navigateToGuarantorListScreen(loanId: Long) {
    navigate(GuarantorNavigation.GuarantorList.passArguments(loanId = loanId.toString()))
}

/**
 * Builds the navigation graph for the Guarantor feature.
 *
 * This function sets up the navigation structure for the guarantor management flow,
 * including the list, add, and detail screens with their respective navigation connections.
 *
 * @param navController The NavHostController used for navigation within the graph.
 */
fun NavGraphBuilder.guarantorNavGraph(
    navController: NavHostController,
) {
    navigation(
        route = GuarantorNavigation.GuarantorScreenBase.route,
        startDestination = GuarantorNavigation.GuarantorList.route,
    ) {
        addGuarantorRoute(
            navigateBack = navController::popBackStack,
        )

        listGuarantorRoute(
            navigateBack = navController::popBackStack,
            addGuarantor = { loanId ->
                navController.navigate(
                    GuarantorNavigation.GuarantorAdd.passArguments(
                        index = -1,
                        loanId = loanId,
                    ),
                )
            },
            onGuarantorClicked = { index, loanId ->
                navController.navigate(
                    GuarantorNavigation.GuarantorDetails.passArguments(
                        index = index,
                        loanId = loanId,
                    ),
                )
            },
        )

        detailGuarantorRoute(
            navigateBack = navController::popBackStack,
            updateGuarantor = { index, loanId ->
                navController.navigate(
                    GuarantorNavigation.GuarantorAdd.passArguments(
                        index = index,
                        loanId = loanId,
                    ),
                )
            },
        )
    }
}

/**
 * Defines the composable destination for the Guarantor List screen
 * within the navigation graph.
 *
 * This function sets up the route, the screen content ([GuarantorListScreen]),
 * and wires up the navigation callbacks for actions initiated from this screen.
 *
 * @param navigateBack A lambda function to handle the back navigation event.
 * @param addGuarantor A lambda to navigate to the add guarantor screen with the loan ID.
 * @param onGuarantorClicked A lambda to navigate to guarantor details with index and loan ID.
 */
fun NavGraphBuilder.listGuarantorRoute(
    navigateBack: () -> Unit,
    addGuarantor: (Long) -> Unit,
    onGuarantorClicked: (Int, Long) -> Unit,
) {
    composable(
        route = GuarantorNavigation.GuarantorList.route,
        arguments = listOf(
            navArgument(name = LOAN_ID) { type = NavType.LongType },
        ),
    ) {
        GuarantorListScreen(
            navigateBack = navigateBack,
            addGuarantor = addGuarantor,
            onGuarantorClicked = onGuarantorClicked,
        )
    }
}

/**
 * Defines the composable destination for the Guarantor Detail screen
 * within the navigation graph.
 *
 * This function sets up the route, the screen content ([GuarantorDetailScreen]),
 * and wires up the navigation callbacks for actions initiated from this screen.
 *
 * @param navigateBack A lambda function to handle the back navigation event.
 * @param updateGuarantor A lambda to navigate to the add guarantor screen for editing,
 *   passing the guarantor index and loan ID.
 */
fun NavGraphBuilder.detailGuarantorRoute(
    navigateBack: () -> Unit,
    updateGuarantor: (index: Int, loanId: Long) -> Unit,
) {
    composable(
        route = GuarantorNavigation.GuarantorDetails.route,
        arguments = listOf(
            navArgument(name = INDEX) { type = NavType.IntType },
            navArgument(name = LOAN_ID) { type = NavType.LongType },
        ),
    ) {
        GuarantorDetailScreen(
            navigateBack = navigateBack,
            updateGuarantor = updateGuarantor,
        )
    }
}

/**
 * Defines the composable destination for the Add Guarantor screen
 * within the navigation graph.
 *
 * This function sets up the route, the screen content ([AddGuarantorScreen]),
 * and wires up the navigation callbacks for actions initiated from this screen.
 *
 * @param navigateBack A lambda function to handle the back navigation event.
 */
fun NavGraphBuilder.addGuarantorRoute(
    navigateBack: () -> Unit,
) {
    composable(
        route = GuarantorNavigation.GuarantorAdd.route,
        arguments = listOf(
            navArgument(name = INDEX) {
                type = NavType.IntType
                defaultValue = -1
            },
            navArgument(name = LOAN_ID) { type = NavType.LongType },
        ),
    ) {
        AddGuarantorScreen(
            navigateBack = navigateBack,
        )
    }
}
