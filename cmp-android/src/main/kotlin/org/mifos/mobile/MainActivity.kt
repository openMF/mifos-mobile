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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.shared.MainUiState
import org.mifos.mobile.shared.MifosMobileSharedApp
import org.mifos.mobile.shared.MifosMobileViewModel

class MainActivity : ComponentActivity() {

    private val networkMonitor: NetworkMonitor by inject()

    private val viewModel: MifosMobileViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        var uiState: MainUiState by mutableStateOf(MainUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach { uiState = it }
                    .collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                MainUiState.Loading -> true
                is MainUiState.Error -> true
                is MainUiState.Success -> false
            }
        }

        enableEdgeToEdge()

        setContent {
            CompositionLocalProvider {
                MifosMobileTheme {
                    MifosMobileSharedApp(networkMonitor = networkMonitor)
                }
            }
        }
    }
}
