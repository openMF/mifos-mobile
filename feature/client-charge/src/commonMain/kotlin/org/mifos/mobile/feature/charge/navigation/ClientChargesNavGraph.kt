/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.ui.composableWithPushTransitions
import org.mifos.mobile.feature.charge.screens.ClientChargeScreen

@Serializable
data class ClientChargesRoute(
    val chargeType: String,
    val chargeTypeId: Long,
)

@Serializable
data object ClientChargesNavGraphRoute

fun NavGraphBuilder.clientChargeNavGraph(
    navController: NavController,
    navigateBack: () -> Unit,
) {
    navigation<ClientChargesNavGraphRoute>(
        startDestination = ClientChargesRoute(ChargeType.SAVINGS.name, -1),
    ) {
        clientChargesScreen(
            onNavigateBack = navigateBack,
            navigateToChargeDetailsScreen = {
                navController.navigateToChargesDetailsScreen(it)
            },
        )
        chargesDetailsDestination(
            onNavigateBack = navController::popBackStack,
        )
    }
}

fun NavGraphBuilder.clientChargesScreen(
    onNavigateBack: () -> Unit,
    navigateToChargeDetailsScreen: (charge: Charge) -> Unit,
) {
    composableWithPushTransitions<ClientChargesRoute> {
        ClientChargeScreen(
            navigateBack = onNavigateBack,
            onChargeClick = navigateToChargeDetailsScreen,
        )
    }
}

fun NavController.navigateToClientChargeScreen(
    chargeType: String,
    chargeTypeId: Long,
) {
    this.navigate(ClientChargesRoute(chargeType, chargeTypeId))
}
