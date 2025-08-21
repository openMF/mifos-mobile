/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp.navigation.rootnav.RootNavScreen
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.NetworkBanner

@Composable
fun ComposeApp(
    handleThemeMode: (osValue: Int) -> Unit,
    handleAppLocale: (locale: String?) -> Unit,
    onSplashScreenRemoved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ComposeAppViewModel = koinViewModel(),
) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(eventFlow = viewModel.eventFlow) { event ->
        when (event) {
            is AppEvent.ShowToast -> {}
            is AppEvent.UpdateAppLocale -> handleAppLocale(event.localeName)
            is AppEvent.UpdateAppTheme -> handleThemeMode(event.osValue)
        }
    }

    MifosMobileTheme(
        darkTheme = uiState.darkTheme,
        androidTheme = uiState.isAndroidTheme,
        shouldDisplayDynamicTheming = uiState.isDynamicColorsEnabled,
    ) {
        Column(modifier = modifier.fillMaxSize().statusBarsPadding()) {
            NetworkBanner(
                bannerState = uiState.networkBanner,
                modifier = Modifier.fillMaxWidth(),
            )

            RootNavScreen(
                modifier = Modifier,
                onSplashScreenRemoved = onSplashScreenRemoved,
            )
        }
    }
}
