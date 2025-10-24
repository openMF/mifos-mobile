/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:Suppress("MatchingDeclarationName")

package org.mifos.mobile.feature.charge.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.feature.charge.chargeDetails.chargesDetailsDestination
import org.mifos.mobile.feature.charge.chargeDetails.navigateToChargesDetailsScreen
import org.mifos.mobile.feature.charge.charges.ClientChargesRoute
import org.mifos.mobile.feature.charge.charges.clientChargesScreen

@Serializable
data object ClientChargesNavGraphRoute

/**
 * Navigation graph for the Client Charges Screen.
 *
 * @param navController The NavController to be used for navigation.
 */
fun NavGraphBuilder.clientChargeNavGraph(
    navController: NavController,
//    navigateBack: () -> Unit,
) {
    navigation<ClientChargesNavGraphRoute>(
        startDestination = ClientChargesRoute(ChargeType.CLIENT.name),
    ) {
        clientChargesScreen(
            onNavigateBack = navController::popBackStack,
            navigateToChargeDetailsScreen = {
                navController.navigateToChargesDetailsScreen(it)
            },
        )
        chargesDetailsDestination(
            onNavigateBack = navController::popBackStack,
        )
    }
}

fun NavController.navigateToChargeGraph(navOptions: NavOptions? = null) {
    this.navigate(ClientChargesNavGraphRoute, navOptions)
}
