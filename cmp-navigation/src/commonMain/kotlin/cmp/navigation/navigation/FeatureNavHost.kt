/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import cmp.navigation.ui.AppState
import org.mifos.mobile.feature.loan.navigation.LoanNavigation
import org.mifos.mobile.feature.loan.navigation.loanNavGraph

@Composable
internal fun FeatureNavHost(
    appState: AppState,
//    onClickLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        route = NavGraphRoute.MAIN_GRAPH,
        startDestination = LoanNavigation.LoanBase.route,
        navController = appState.navController,
        modifier = modifier,
    ) {
//        homeScreen()
        loanNavGraph(
            navController = appState.navController,
            viewQr = { },
            viewGuarantor = { },
            viewCharges = { },
            makePayment = { _: Long, _: Double?, _: String -> },
//            viewQr = navController::navigateToQrDisplayScreen,
//            viewGuarantor = navController::navigateToGuarantorScreen,
//            viewCharges = navController::navigateToClientChargeScreen,
//            makePayment = navController::navigateToSavingsMakeTransfer,
        )
    }
}
