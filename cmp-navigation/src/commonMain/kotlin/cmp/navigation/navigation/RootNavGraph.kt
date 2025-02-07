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
import cmp.navigation.navigation.NavGraphRoute.MAIN_GRAPH
import cmp.navigation.ui.App
import org.mifos.mobile.core.data.util.NetworkMonitor

@Composable
fun RootNavGraph(
    networkMonitor: NetworkMonitor,
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHostController,
        startDestination = MAIN_GRAPH,
        route = NavGraphRoute.ROOT_GRAPH,
        modifier = modifier,
    ) {
        composable(MAIN_GRAPH) {
            App(
                modifier = modifier,
                networkMonitor = networkMonitor,
            )
        }
    }
}
