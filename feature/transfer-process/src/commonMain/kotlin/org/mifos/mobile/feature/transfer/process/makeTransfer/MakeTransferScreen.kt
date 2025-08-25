/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transfer.process.makeTransfer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.transfer_process.generated.resources.Res
import mifos_mobile.feature.transfer_process.generated.resources.amount
import mifos_mobile.feature.transfer_process.generated.resources.error_description
import mifos_mobile.feature.transfer_process.generated.resources.feature_make_transfer_error_server
import mifos_mobile.feature.transfer_process.generated.resources.make_transfer
import mifos_mobile.feature.transfer_process.generated.resources.pay_to
import mifos_mobile.feature.transfer_process.generated.resources.remarks
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.component.MifosDropDownDoubleTextField
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPayFromDropdownUI
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun MakeTransferScreen(
    navigateBack: () -> Unit,
    navigateToTransferScreen: (ReviewTransferPayload, TransferType, String) -> Unit,
    viewModel: MakeTransferViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            MakeTransferEvent.NavigateBack -> {
                navigateBack.invoke()
            }
            is MakeTransferEvent.NavigateToTransferScreen -> {
                navigateToTransferScreen(event.reviewTransferPayload, event.transferType, event.destination)
            }
        }
    }

    MakeTransferScreenContent(
        state = state,
        onAction = remember(viewModel) {
            {
                viewModel.trySendAction(it)
            }
        },
    )

    MakeTransferDialog(
        state = state,
        onAction = remember(viewModel) {
            {
                viewModel.trySendAction(it)
            }
        },
    )
}

@Composable
internal fun MakeTransferScreenContent(
    state: MakeTransferState,
    onAction: (MakeTransferAction) -> Unit,
) {
    MifosElevatedScaffold(
        topBarTitle = stringResource(Res.string.make_transfer),
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
        onNavigateBack = {
            onAction(MakeTransferAction.NavigateBack)
        },
    ) {
        when (state.uiState) {
            is MakeTransferState.MakeTransferScreenState.Loading -> {
                MifosProgressIndicator()
            }

            is MakeTransferState.MakeTransferScreenState.OverlayLoading -> {
                MifosProgressIndicatorOverlay()
            }

            is MakeTransferState.MakeTransferScreenState.Error -> {
                MifosErrorComponent(
                    message = stringResource(Res.string.feature_make_transfer_error_server),
                    isRetryEnabled = true,
                    onRetry = { onAction(MakeTransferAction.OnRetry) },
                )
            }

            is MakeTransferState.MakeTransferScreenState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(MakeTransferAction.OnRetry) },
                )
            }

            is MakeTransferState.MakeTransferScreenState.Success -> {
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            horizontal = DesignToken.padding.large,
                            vertical = DesignToken.padding.extraLargeIncreased,
                        ),
                    verticalArrangement = Arrangement.spacedBy(DesignToken.padding.largeIncreased),
                ) {
                    MifosDropDownDoubleTextField(
                        optionsList = state.toAccountOptions.map
                            { Pair(it.accountNo ?: "", it.clientName ?: "") },
                        selectedOption = state.toAccount?.accountNo ?: "",
                        isEnabled = state.outstandingBalance == null,
                        labelResId = Res.string.pay_to,
                        onClick = { index, _ ->
                            onAction(
                                MakeTransferAction.OnToAccountSelected(
                                    state.toAccountOptions[index].accountNo ?: "",
                                ),
                            )
                        },
                    )

                    MifosPayFromDropdownUI(
                        accounts = state.fromAccountOptions.map
                            { Pair(it.accountNo ?: "", it.clientName ?: "") },
                        onAccountSelected = { account, balance ->
                            onAction(MakeTransferAction.OnFromAccountSelected(account))
                        },
                    )

                    MifosOutlinedTextField(
                        value = state.amount,
                        onValueChange = { onAction(MakeTransferAction.OnAmountChanged(it)) },
                        label = stringResource(Res.string.amount),
                        shape = DesignToken.shapes.medium,
                        textStyle = MifosTypography.bodyLarge,
                        config = MifosTextFieldConfig(
                            enabled = state.outstandingBalance == null,
                            isError = state.amountError != null,
                            errorText = state.amountError?.let { stringResource(it) },
                            trailingIcon = if (state.amountError != null) {
                                {
                                    Icon(
                                        imageVector = MifosIcons.ErrorCircle,
                                        contentDescription = stringResource(Res.string.error_description),
                                        tint = MaterialTheme.colorScheme.error,
                                    )
                                }
                            } else {
                                null
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                            ),
                        ),
                    )

                    MifosOutlinedTextField(
                        value = state.remark,
                        onValueChange = { onAction(MakeTransferAction.OnRemarksChanged(it)) },
                        label = stringResource(Res.string.remarks),
                        shape = DesignToken.shapes.medium,
                        textStyle = MifosTypography.bodyLarge,
                        config = MifosTextFieldConfig(
                            isError = state.remarkError != null,
                            errorText = state.remarkError?.let {
                                stringResource(it)
                            },
                            trailingIcon = if (state.remarkError != null) {
                                {
                                    Icon(
                                        imageVector = MifosIcons.ErrorCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                    )
                                }
                            } else {
                                null
                            },
                        ),
                    )

                    MifosButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(DesignToken.sizes.buttonHeight),
                        onClick = {
                            onAction(MakeTransferAction.OnMakeTransferClicked)
                        },
                        text = {
                            Text(
                                text = stringResource(Res.string.make_transfer),
                                style = MifosTypography.titleMedium,
                            )
                        },
                        enabled = state.isEnabled,
                    )
                }
            }

            null -> {}
        }
    }
}

@Composable
internal fun MakeTransferDialog(
    state: MakeTransferState,
    onAction: (MakeTransferAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state.dialogState) {
        is MakeTransferState.DialogState.Error -> {
            MifosErrorComponent(
                isNetworkConnected = state.networkStatus,
                message = state.dialogState.message,
                isRetryEnabled = true,
                onRetry = { onAction(MakeTransferAction.OnRetry) },
                modifier = modifier,
            )
        }
        null -> {}
    }
}

@Composable
@Preview
fun MakeTransferScreenPreview() {
    MifosMobileTheme {
        MakeTransferScreenContent(
            state = MakeTransferState(),
            onAction = {},
        )
    }
}
