/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.mifos.library.passcode.navigateToPasscodeScreen
import org.mifos.mobile.feature.auth.navigation.authenticationNavGraph
import org.mifos.mobile.navigation.MifosNavGraph.AUTH_GRAPH
import org.mifos.mobile.ui.MifosApp
import org.mifos.mobile.ui.MifosMobileState

@Composable
internal fun RootNavGraph(
    appState: MifosMobileState,
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
            navigateToPasscodeScreen = navHostController::navigateToPasscodeScreen,
        )

        passcodeNavGraph(navHostController)

        composable(MifosNavGraph.MAIN_GRAPH) {
            MifosApp(
                appState = appState,
                onClickLogout = onClickLogout,
            )
        }
    }
}
