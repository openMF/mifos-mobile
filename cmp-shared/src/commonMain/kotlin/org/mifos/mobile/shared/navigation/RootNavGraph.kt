/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.shared.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.feature.auth.navigation.authenticationNavGraph
import org.mifos.mobile.shared.navigation.MifosNavGraph.AUTH_GRAPH
import org.mifos.mobile.shared.ui.MifosApp

@Composable
internal fun RootNavGraph(
//    appState: MifosMobileState,
    networkMonitor: NetworkMonitor,
    navHostController: NavHostController,
    startDestination: String,
    onClickLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        route = MifosNavGraph.ROOT_GRAPH,
        modifier = modifier,
    ) {
        authenticationNavGraph(
            navController = navHostController,
            route = AUTH_GRAPH,
//            navigateToPasscodeScreen = navHostController::navigateToPasscodeScreen,
            navigateToPasscodeScreen = { },
        )

        passcodeNavGraph(navHostController)

        composable(MifosNavGraph.MAIN_GRAPH) {
            MifosApp(
                networkMonitor = networkMonitor,
                onClickLogout = onClickLogout,
            )
        }
    }
}
