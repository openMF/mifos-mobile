/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import cmp.navigation.rootnav.RootNavScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.SessionManager
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.MifosThemeConfig
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.NetworkBanner
import org.mifos.mobile.core.ui.utils.SessionHandler
import org.mifos.mobile.navigation.generated.resources.Res
import org.mifos.mobile.navigation.generated.resources.session_expired_message
import org.mifos.mobile.navigation.generated.resources.session_expired_title
import template.core.base.designsystem.theme.KptTheme

@Composable
fun ComposeApp(
    handleThemeMode: (osValue: Int) -> Unit,
    handleAppLocale: (locale: String?) -> Unit,
    onSplashScreenRemoved: () -> Unit,
    modifier: Modifier = Modifier,
    sessionManager: SessionManager = koinInject(),
    viewModel: ComposeAppViewModel = koinViewModel(),
) {
    val navController = rememberNavController()
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    var wasBackgrounded by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                wasBackgrounded = true
                viewModel.trySendAction(AppAction.LockApp)
            } else if (event == Lifecycle.Event.ON_START) {
                if (wasBackgrounded) {
                    viewModel.trySendAction(AppAction.LockApp)
                    wasBackgrounded = false
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            sessionManager.stopSession()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val isSessionExpired by sessionManager.isExpired.collectAsStateWithLifecycle()
    EventsEffect(eventFlow = viewModel.eventFlow) { event ->
        when (event) {
            is AppEvent.ShowToast -> {}
            is AppEvent.UpdateAppLocale -> handleAppLocale(event.localeName)
            is AppEvent.UpdateAppTheme -> handleThemeMode(event.osValue)
        }
    }
    val isSystemInDarkTheme = isSystemInDarkTheme()

    LaunchedEffect(isSystemInDarkTheme, uiState.themeConfig) {
        if (uiState.themeConfig == MifosThemeConfig.FOLLOW_SYSTEM) {
            viewModel.trySendAction(AppAction.Internal.SystemThemeUpdate(isSystemInDarkTheme))
        }
    }

    MifosMobileTheme(
        darkTheme = uiState.darkTheme,
        androidTheme = uiState.isAndroidTheme,
        shouldDisplayDynamicTheming = uiState.isDynamicColorsEnabled,
    ) {
        val dialogState = if (isSessionExpired) {
            BasicDialogState.Shown(
                title = stringResource(Res.string.session_expired_title),
                message = stringResource(Res.string.session_expired_message),
            )
        } else {
            BasicDialogState.Hidden
        }

        if (dialogState is BasicDialogState.Shown) {
            MifosBasicDialog(
                visibilityState = dialogState,
                onDismissRequest = {
                    viewModel.trySendAction(AppAction.SessionExpired)
                },
            )
        }

        SessionHandler(
            sessionManager = sessionManager,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(KptTheme.colorScheme.surface),
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                ) {
                    NetworkBanner(
                        bannerState = uiState.networkBanner,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    RootNavScreen(
                        navController = navController,
                        modifier = Modifier,
                        onSplashScreenRemoved = onSplashScreenRemoved,
                    )
                }
            }
        }
    }
}
