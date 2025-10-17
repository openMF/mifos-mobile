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

package org.mifos.mobile.feature.charge.charges

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.ui.composableWithPushTransitions

/**
 * ClientChargesRoute is a data class that represents the route for the Client Charges Screen.
 *
 * @param chargeType Type of the charge.
 * @param chargeTypeId ID of the charge.
 */
@Serializable
data class ClientChargesRoute(
    val chargeType: String,
    val chargeTypeId: Long? = null,
)

/**
 * Navigates to the Client Charges Screen.
 *
 * @param chargeType Type of the charge.
 * @param chargeTypeId ID of the charge.
 */
fun NavController.navigateToClientChargeScreen(
    chargeType: String,
    chargeTypeId: Long?,
) {
    this.navigate(ClientChargesRoute(chargeType, chargeTypeId))
}

/**
 * Composable function that displays the Client Charges Screen.
 *
 * @param onNavigateBack A lambda function that is called when the user navigates back.
 * @param navigateToChargeDetailsScreen A lambda function that is called when the user clicks on a charge.
 */
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
