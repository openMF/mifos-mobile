/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.client_charge.generated.resources.Res
import mifos_mobile.feature.client_charge.generated.resources.database_warning
import mifos_mobile.feature.client_charge.generated.resources.error_no_charge
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.LoadingDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosLoadingDialog
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.charge.components.ClientChargeItem
import org.mifos.mobile.feature.charge.viewmodel.ClientChargeAction
import org.mifos.mobile.feature.charge.viewmodel.ClientChargeEvent
import org.mifos.mobile.feature.charge.viewmodel.ClientChargeState
import org.mifos.mobile.feature.charge.viewmodel.ClientChargeViewModel

@Composable
internal fun ClientChargeScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClientChargeViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is ClientChargeEvent.Navigate -> navigateBack.invoke()

            is ClientChargeEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }
    ClientChargeScreen(
        modifier = modifier,
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    ClientChargeDialogs(
        dialogState = state.dialogState,
        onDismissRequest = remember(viewModel) {
            { viewModel.trySendAction(ClientChargeAction.OnDismissDialog) }
        },
    )
}

@Composable
private fun ClientChargeScreen(
    state: ClientChargeState,
    modifier: Modifier = Modifier,
    onAction: (ClientChargeAction) -> Unit,
) {
    MifosElevatedScaffold(
        topBarTitle = stringResource(state.topBarTitleResId),
        onNavigateBack = { onAction(ClientChargeAction.OnNavigate) },
        modifier = modifier,
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                if (state.dialogState == null) {
                    ClientChargeContent(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        chargesList = state.charges,
                    )
                }
            }
        },
    )
}

@Composable
private fun ClientChargeContent(
    chargesList: List<Charge>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items = chargesList) { charge ->
            ClientChargeItem(charge = charge)
        }
    }
}

@Composable
private fun ClientChargeDialogs(
    dialogState: ClientChargeState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is ClientChargeState.DialogState.Loading -> {
            MifosLoadingDialog(
                visibilityState = LoadingDialogState.Shown,
            )
        }

        is ClientChargeState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = dialogState.message,
                ),
                onDismissRequest = onDismissRequest,
            )
        }

        ClientChargeState.DialogState.Empty -> {
            EmptyDataView(
                modifier = Modifier.fillMaxSize(),
                image = Res.drawable.database_warning,
                error = Res.string.error_no_charge,
            )
        }

        null -> Unit
    }
}

@Preview
@Composable
private fun ClientChargeScreenPreview() {
    MifosMobileTheme {
        ClientChargeScreen(
            modifier = Modifier,
            state = ClientChargeState(dialogState = null, isOnline = false),
            onAction = { },
        )
    }
}
