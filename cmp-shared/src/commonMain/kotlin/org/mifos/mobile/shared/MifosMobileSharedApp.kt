/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.shared.navigation.MifosNavGraph.AUTH_GRAPH
import org.mifos.mobile.shared.navigation.MifosNavGraph.PASSCODE_GRAPH
import org.mifos.mobile.shared.navigation.RootNavGraph

@Composable
fun MifosMobileSharedApp(
    modifier: Modifier = Modifier,
    networkMonitor: NetworkMonitor = koinInject(),
) {
    MifosApp(networkMonitor, modifier)
}

@Composable
private fun MifosApp(
    networkMonitor: NetworkMonitor,
    modifier: Modifier = Modifier,
    viewModel: MifosMobileViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    val navDestination = when (uiState) {
        is MainUiState.Loading -> AUTH_GRAPH
        is MainUiState.Success -> if ((uiState as MainUiState.Success).userData.isAuthenticated) {
            PASSCODE_GRAPH
        } else {
            AUTH_GRAPH
        }

        else -> AUTH_GRAPH
    }

    MifosMobileTheme {
        RootNavGraph(
            navHostController = navController,
            startDestination = navDestination,
            networkMonitor = networkMonitor,
            modifier = modifier,
            onClickLogout = {
                viewModel.logOut()
                navController.navigate(AUTH_GRAPH) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            },
        )
    }
}
