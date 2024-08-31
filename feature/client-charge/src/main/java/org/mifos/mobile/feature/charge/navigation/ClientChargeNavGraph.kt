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
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.feature.charge.screens.ClientChargeScreen

fun NavController.navigateToClientChargeScreen(
    chargeType: ChargeType,
) {
    navigate(ClientChargeNavigation.ClientChargeScreen.passArguments(chargeType))
}

fun NavGraphBuilder.clientChargeNavGraph(
    navigateBack: () -> Unit,
) {
    navigation(
        startDestination = ClientChargeNavigation.ClientChargeScreen.route,
        route = ClientChargeNavigation.ClientChargeBase.route,
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
        route = ClientChargeNavigation.ClientChargeScreen.route,
        arguments = listOf(navArgument(CHARGE_TYPE) { type = NavType.StringType }),
    ) {
        ClientChargeScreen(
            navigateBack = navigateBack,
        )
    }
}
