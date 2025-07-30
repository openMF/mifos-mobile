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

@Serializable
data class ClientChargesRoute(
    val chargeType: String,
    val chargeTypeId: Long? = null,
)

fun NavController.navigateToClientChargeScreen(
    chargeType: String,
    chargeTypeId: Long?,
) {
    this.navigate(ClientChargesRoute(chargeType, chargeTypeId))
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
