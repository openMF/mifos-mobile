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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.common.Constants.CHARGE_TYPE
import org.mifos.mobile.core.common.Constants.CHARGE_TYPE_ID
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.ui.composableWithPushTransitions
import org.mifos.mobile.feature.charge.screens.ClientChargeItem2
import org.mifos.mobile.feature.charge.screens.ClientChargeScreen



@Serializable
data class ClientChargesRoute(
    val chargeType: String,
    val chargeTypeId: Long
)

//@Serializable
//data object ClientChargesRoute

@Serializable
data object ClientChargesNavGraphRoute

fun NavGraphBuilder.clientChargeNavGraph(
    navigateBack: () -> Unit,
){
    navigation<ClientChargesNavGraphRoute>(
        startDestination = ClientChargesRoute("",-1),
    ){
        clientChargesScreen(onNavigateBack = navigateBack)
    }
}

fun NavGraphBuilder.clientChargesScreen(
    onNavigateBack: () -> Unit,
) {
    composableWithPushTransitions<ClientChargesRoute> {
        Column {
            Spacer(Modifier.height(40.dp))
            ClientChargeItem2(charge= Charge())
        }
    }
}

fun NavController.navigateToClientChargeScreen(
    chargeType: String,
    chargeTypeId: Long)
{
    this.navigate(ClientChargesRoute(chargeType,chargeTypeId))
}
