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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp.navigation.rootnav.RootNavScreen
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.EventsEffect

//@Composable
//fun ComposeApp(
//    modifier: Modifier = Modifier,
//    networkMonitor: NetworkMonitor = koinInject(),
//    viewModel: ComposeAppViewModel = koinViewModel(),
//) {
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//    val navController = rememberNavController()
//
//    val navDestination = when (uiState) {
//        is MainUiState.Loading -> AUTH_GRAPH
//        is MainUiState.Success -> if ((uiState as MainUiState.Success).userData.isAuthenticated) {
//            PASSCODE_GRAPH
//        } else {
//            AUTH_GRAPH
//        }
//
//        else -> AUTH_GRAPH
//    }
//
//    val isDarkMode = when (uiState) {
//        is MainUiState.Success -> when ((uiState as MainUiState.Success).appTheme) {
//            AppTheme.SYSTEM -> isSystemInDarkTheme()
//            AppTheme.LIGHT -> false
//            AppTheme.DARK -> true
//        }
//        else -> true
//    }
//
//    MifosMobileTheme(isDarkMode) {
//        RootNavGraph(
//            modifier = modifier.fillMaxSize(),
//            networkMonitor = networkMonitor,
//            navHostController = navController,
//            startDestination = navDestination,
//            onClickLogout = {
//                viewModel.logOut()
//                navController.navigate(AUTH_GRAPH) {
//                    popUpTo(navController.graph.id) {
//                        inclusive = true
//                    }
//                }
//            },
//        )
//    }
//}



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

    MifosMobileTheme (
        darkTheme = uiState.darkTheme,
        androidTheme = uiState.isAndroidTheme,
        shouldDisplayDynamicTheming = uiState.isDynamicColorsEnabled,
    ) {
        RootNavScreen(
            modifier = modifier,
            onSplashScreenRemoved = onSplashScreenRemoved,
        )
    }
}
