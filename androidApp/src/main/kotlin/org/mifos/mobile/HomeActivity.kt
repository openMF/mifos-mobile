/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.mifos.mobile.HomeActivityUiState.Success
import org.mifos.mobile.core.data.utils.NetworkMonitor
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.darkScrim
import org.mifos.mobile.core.designsystem.theme.lightScrim
import org.mifos.mobile.navigation.MifosNavGraph.AUTH_GRAPH
import org.mifos.mobile.navigation.MifosNavGraph.PASSCODE_GRAPH
import org.mifos.mobile.navigation.RootNavGraph
import org.mifos.mobile.ui.rememberMifosMobileState
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    private val viewModel: HomeActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        var uiState: HomeActivityUiState by mutableStateOf(HomeActivityUiState.Loading)

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach { uiState = it }
                    .collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                HomeActivityUiState.Loading -> true
                is Success -> false
            }
        }

        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            val appState = rememberMifosMobileState(networkMonitor = networkMonitor)
            val darkTheme= isSystemInDarkTheme()
            val navDestination = when (uiState) {
                is Success -> if ((uiState as Success).userData.isAuthenticated) {
                    PASSCODE_GRAPH
                } else {
                    AUTH_GRAPH
                }

                else -> AUTH_GRAPH
            }

            DisposableEffect(darkTheme) {
                window?.statusBarColor = if (darkTheme) darkScrim.toArgb() else lightScrim.toArgb()
                window?.navigationBarColor = if (darkTheme) darkScrim.toArgb() else lightScrim.toArgb()
                onDispose {}
            }

            CompositionLocalProvider {
                MifosMobileTheme {
                    RootNavGraph(
                        appState = appState,
                        navHostController = navController,
                        startDestination = navDestination,
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
        }
    }
}
