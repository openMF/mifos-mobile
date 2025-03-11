/*
 * Copyright 2024 Mifos Initiative
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

fun NavController.navigateToGuarantorScreen(loanId: Long) {
    navigate(GuarantorNavigation.GuarantorScreenBase.passArguments(loanId = loanId.toString()))
}

fun NavController.navigateToGuarantorListScreen(loanId: Long) {
    navigate(GuarantorNavigation.GuarantorList.passArguments(loanId = loanId.toString()))
}

fun NavGraphBuilder.guarantorNavGraph(
    navController: NavHostController,
) {
    navigation(
        startDestination = GuarantorNavigation.GuarantorList.route,
        route = GuarantorNavigation.GuarantorScreenBase.route,
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

fun NavGraphBuilder.listGuarantorRoute(
    navigateBack: () -> Unit,
    addGuarantor: (Long) -> Unit,
    onGuarantorClicked: (Int, Long) -> Unit,
) {
    composable(
        route = GuarantorNavigation.GuarantorList.route,
        arguments = listOf(
            navArgument(name = LOAN_ID) { type = NavType.StringType },
        ),
    ) {
        GuarantorListScreen(
            navigateBack = navigateBack,
            addGuarantor = addGuarantor,
            onGuarantorClicked = onGuarantorClicked,
        )
    }
}

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
