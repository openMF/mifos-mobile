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
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import org.mifos.mobile.core.common.Constants.CHARGE_TYPE
import org.mifos.mobile.core.common.Constants.CHARGE_TYPE_ID
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.feature.charge.screens.ClientChargeScreen

fun NavController.navigateToClientChargeScreen(
    chargeType: ChargeType,
    chargeTypeId: Long? = null,
) {
    navigate(ChargeNavigation.ChargeRouteScreen.passArguments(chargeType, chargeTypeId))
}

fun NavGraphBuilder.clientChargeNavGraph(
    navigateBack: () -> Unit,
) {
    navigation(
        startDestination = ChargeNavigation.ChargeRouteScreen.route,
        route = ChargeNavigation.ChargeRouteBase.route,
    ) {
        clientChargeScreenRoute(
            navigateBack = navigateBack,
        )
    }
}

fun NavGraphBuilder.clientChargeScreenRoute(
    navigateBack: () -> Unit,
) {
    composable(
        route = ChargeNavigation.ChargeRouteScreen.route,
        arguments = listOf(
            navArgument(CHARGE_TYPE) { type = NavType.StringType },
            navArgument(CHARGE_TYPE_ID) { type = NavType.LongType },
        ),
    ) {
        ClientChargeScreen(navigateBack = navigateBack)
    }
}
