/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transfer.process

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.transfer_process.generated.resources.Res
import mifos_mobile.feature.transfer_process.generated.resources.amount
import mifos_mobile.feature.transfer_process.generated.resources.cancel
import mifos_mobile.feature.transfer_process.generated.resources.date
import mifos_mobile.feature.transfer_process.generated.resources.pay_from
import mifos_mobile.feature.transfer_process.generated.resources.pay_to
import mifos_mobile.feature.transfer_process.generated.resources.remark
import mifos_mobile.feature.transfer_process.generated.resources.transfer
import mifos_mobile.feature.transfer_process.generated.resources.transfer_from_savings
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosCard
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.TransferSuccessDestination
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun TransferProcessScreen(
    navigateBack: () -> Unit,
    onTransferSuccessNavigate: (TransferSuccessDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransferProcessViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            TransferProcessEvent.Navigate -> navigateBack.invoke()
            is TransferProcessEvent.TransferSuccess -> {
                onTransferSuccessNavigate(event.destination)
            }
            is TransferProcessEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }
    TransferProcessScreen(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        modifier = modifier,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
private fun TransferProcessDialog(
    state: TransferProcessState,
) {
    when (state.dialogState) {
        TransferProcessState.DialogState.Loading -> MifosProgressIndicatorOverlay()
        is TransferProcessState.DialogState.Error -> MifosErrorComponent(
            isNetworkConnected = state.isOnline,
        )
        null -> Unit
    }
}

@Composable
private fun TransferProcessScreen(
    state: TransferProcessState,
    onAction: (TransferProcessAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        topBarTitle = stringResource(Res.string.transfer),
        backPress = { onAction(TransferProcessAction.OnNavigate) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
            ) {
                TransferProcessContent(
                    state = state,
                    onAction = onAction,
                )
            }
        },
    )
    TransferProcessDialog(
        state = state,
    )
}

@Composable
private fun TransferProcessContent(
    state: TransferProcessState,
    onAction: (TransferProcessAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        MifosCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(Res.string.amount),
                    )

                    Text(text = state.transferPayload?.transferAmount.toString())
                }

                Text(
                    text = stringResource(Res.string.transfer_from_savings),
                    fontWeight = FontWeight(500),
                )

                Text(
                    text = stringResource(Res.string.pay_to),
                    modifier = Modifier.padding(top = 8.dp),
                )

                Text(
                    text = state.transferPayload?.toAccountId.toString(),
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp),
                )

                HorizontalDivider()

                Text(
                    text = stringResource(Res.string.pay_from),
                    modifier = Modifier.padding(top = 8.dp),
                )

                Text(
                    text = state.transferPayload?.fromAccountId.toString(),
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp),
                )

                HorizontalDivider()

                Text(
                    text = stringResource(Res.string.date),
                    modifier = Modifier.padding(top = 8.dp),
                )

                Text(
                    text = state.transferPayload?.transferDate.toString(),
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp),
                )

                HorizontalDivider()

                Text(
                    text = stringResource(Res.string.remark),
                    modifier = Modifier.padding(top = 8.dp),
                )

                Text(
                    text = state.transferPayload?.transferDescription.toString(),
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp),
                )

                HorizontalDivider()

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(30.dp),
                    ) {
                        MifosButton(
                            text = { Text(text = stringResource(Res.string.cancel)) },
                            onClick = { onAction(TransferProcessAction.OnNavigate) },
                        )
                        MifosButton(
                            text = { Text(text = stringResource(Res.string.transfer)) },
                            onClick = { onAction(TransferProcessAction.MakeTransfer) },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TransferProcessScreenPreview() {
    MifosMobileTheme {
        TransferProcessScreen(
            state = TransferProcessState(
                dialogState = null,
                isOnline = false,
                transferType = TransferType.SELF,
            ),
            onAction = { },
            modifier = Modifier,
            snackbarHostState = SnackbarHostState(),
        )
    }
}
