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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cmp.navigation.navigation.NavGraphRoute.AUTH_GRAPH
import cmp.navigation.navigation.NavGraphRoute.MAIN_GRAPH
import cmp.navigation.ui.App
import org.mifos.library.passcode.navigateToPasscodeScreen
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.feature.auth.navigation.authenticationNavGraph

@Composable
fun RootNavGraph(
    networkMonitor: NetworkMonitor,
    navHostController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    onClickLogout: () -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        route = NavGraphRoute.ROOT_GRAPH,
    ) {
        authenticationNavGraph(
            navController = navHostController,
            route = AUTH_GRAPH,
            navigateToPasscodeScreen = navHostController::navigateToPasscodeScreen,

        )
        passcodeNavGraph(navHostController)
        composable(MAIN_GRAPH) {
            App(
                modifier = modifier,
                networkMonitor = networkMonitor,
                onClickLogout = onClickLogout,
            )
        }
    }
}
