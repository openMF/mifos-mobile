/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transfer.process.transferProcess

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.transfer_process.generated.resources.Res
import mifos_mobile.feature.transfer_process.generated.resources.amount
import mifos_mobile.feature.transfer_process.generated.resources.cancel
import mifos_mobile.feature.transfer_process.generated.resources.date
import mifos_mobile.feature.transfer_process.generated.resources.pay_from
import mifos_mobile.feature.transfer_process.generated.resources.pay_to
import mifos_mobile.feature.transfer_process.generated.resources.remark
import mifos_mobile.feature.transfer_process.generated.resources.transfer
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosOutlinedButton
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.component.MifosDetailsCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun TransferProcessScreen(
    navigateBack: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransferProcessViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            TransferProcessEvent.Navigate -> navigateBack.invoke()

            is TransferProcessEvent.NavigateToAuthenticate -> {
                navigateToAuthenticateScreen.invoke()
            }

            is TransferProcessEvent.NavigateToStatus -> {
                navigateToStatusScreen.invoke(
                    event.eventType,
                    event.eventDestination,
                    event.title,
                    event.subtitle,
                    event.buttonText,
                )
            }
        }
    }
    TransferProcessScreen(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        modifier = modifier,
    )
}

@Composable
private fun TransferProcessScreen(
    state: TransferProcessState,
    onAction: (TransferProcessAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        topBarTitle = stringResource(Res.string.transfer),
        onNavigateBack = { onAction(TransferProcessAction.OnNavigate) },
        modifier = modifier.navigationBarsPadding(),
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
    ) {
        if (state.dialogState == null) {
            TransferProcessContent(
                state = state,
                onAction = onAction,
            )
        }
        MakeTransferDialog(
            state = state,
        )
    }
}

@Composable
internal fun MakeTransferDialog(
    state: TransferProcessState,
    modifier: Modifier = Modifier,
) {
    when (state.dialogState) {
        TransferProcessState.DialogState.Loading -> {
            MifosProgressIndicator()
        }
        TransferProcessState.DialogState.Network -> {
            MifosErrorComponent(
                isNetworkConnected = !state.networkUnavailable,
                modifier = modifier,
            )
        }
        null -> {}
    }
}

@Composable
private fun TransferProcessContent(
    state: TransferProcessState,
    onAction: (TransferProcessAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(DesignToken.padding.large)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(DesignToken.padding.large),
    ) {
        MifosDetailsCard(
            keyValuePairs = mapOf(
                Res.string.amount to state.transferPayload?.transferAmount.toString(),
                Res.string.pay_to to state.transferPayload?.toAccountId.toString(),
                Res.string.pay_from to state.transferPayload?.fromAccountId.toString(),
                Res.string.date to state.transferPayload?.transferDate.toString(),
                Res.string.remark to state.transferPayload?.transferDescription.toString(),
            ),
        )

        MifosOutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(DesignToken.sizes.buttonHeight),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            onClick = {
                onAction(TransferProcessAction.OnNavigate)
            },
            shape = DesignToken.shapes.medium,
        ) {
            Text(
                text = stringResource(Res.string.cancel),
                style = MaterialTheme.typography.labelLarge,
            )
        }

        MifosButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(DesignToken.sizes.buttonHeight),
            text = { Text(text = stringResource(Res.string.transfer)) },
            onClick = { onAction(TransferProcessAction.RequestTransfer) },
        )
    }
}

@Preview
@Composable
private fun TransferProcessScreenPreview() {
    MifosMobileTheme {
        TransferProcessScreen(
            state = TransferProcessState(
                transferType = TransferType.SELF,
            ),
            onAction = { },
            modifier = Modifier,
        )
    }
}
