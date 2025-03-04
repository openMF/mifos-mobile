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
import mifos_mobile.feature.home.generated.resources.cancel
import mifos_mobile.feature.home.generated.resources.dialog_logout
import mifos_mobile.feature.home.generated.resources.error_fetching_client
import mifos_mobile.feature.home.generated.resources.logout
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosAlertDialog
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

    HomeScreenDialog(
        dialogState = state.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
    HomeContent(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
private fun HomeScreenDialog(
    dialogState: HomeState.DialogState?,
    onAction: (HomeAction) -> Unit,
) {
    when (dialogState) {
        is HomeState.DialogState.Error -> EmptyDataView(
            icon = MifosIcons.Error,
            error = Res.string.error_fetching_client,
        )

        is HomeState.DialogState.Loading -> MifosProgressIndicator(modifier = Modifier.fillMaxSize())

        is HomeState.DialogState.LogoutConfirmationDialog -> {
            MifosAlertDialog(
                onDismissRequest = { onAction(HomeAction.OnDismissDialog) },
                dismissText = stringResource(Res.string.cancel),
                onConfirmation = { onAction(HomeAction.OnLogoutConfirmed) },
                confirmationText = stringResource(Res.string.logout),
                dialogTitle = stringResource(Res.string.dialog_logout),
                dialogText = "",
            )
        }
        null -> Unit
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    MifosMobileTheme {
        HomeContent(
            state = HomeState(dialogState = null),
            onAction = {},
            modifier = Modifier,
        )
    }
}
