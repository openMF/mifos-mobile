/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.home.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.home.generated.resources.Res
import mifos_mobile.feature.home.generated.resources.error_fetching_client
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.home.navigation.HomeDestinations
import org.mifos.mobile.feature.home.viewmodel.HomeAction
import org.mifos.mobile.feature.home.viewmodel.HomeEvent
import org.mifos.mobile.feature.home.viewmodel.HomeState
import org.mifos.mobile.feature.home.viewmodel.HomeViewModel

@Composable
internal fun HomeScreen(
    callHelpline: () -> Unit,
    mailHelpline: () -> Unit,
    onNavigate: (HomeDestinations) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is HomeEvent.Navigate -> onNavigate(event.destination)
            HomeEvent.CallHelpLine -> callHelpline()
            HomeEvent.MailHelpLine -> mailHelpline()
            is HomeEvent.TotalLoan -> onNavigate(HomeDestinations.LOAN_ACCOUNT)
            is HomeEvent.TotalSavings -> onNavigate(HomeDestinations.SAVINGS_ACCOUNT)
            is HomeEvent.UserProfile -> onNavigate(HomeDestinations.PROFILE)
            is HomeEvent.Notification -> onNavigate(HomeDestinations.NOTIFICATIONS)
        }
    }
    HomeScreen(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
private fun HomeScreen(
    state: HomeState,
    modifier: Modifier = Modifier,
    onAction: (HomeAction) -> Unit,
) {
    when (state.dialogState) {
        is HomeState.DialogState.Error -> EmptyDataView(
            icon = MifosIcons.Error,
            error = Res.string.error_fetching_client,
        )
        HomeState.DialogState.Loading -> MifosProgressIndicator(modifier = Modifier.fillMaxSize())
        else -> HomeContent(
            state = state,
            onAction = onAction,
            modifier = modifier,
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    MifosMobileTheme {
        HomeScreen(
            state = HomeState(dialogState = null),
            onAction = {},
            modifier = Modifier,
        )
    }
}
