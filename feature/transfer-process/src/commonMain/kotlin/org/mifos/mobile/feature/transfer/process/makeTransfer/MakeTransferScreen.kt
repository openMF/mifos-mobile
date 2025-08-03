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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.transfer_process.generated.resources.Res
import mifos_mobile.feature.transfer_process.generated.resources.amount
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
import org.mifos.mobile.core.ui.component.MifosDropDownDoubleTextField
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPayFromDropdownUI
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun MakeTransferScreen(
    navigateBack: () -> Unit,
    navigateToTransferScreen: (ReviewTransferPayload) -> Unit,
    viewModel: MakeTransferViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            MakeTransferEvent.NavigateBack -> {
                navigateBack.invoke()
            }
            is MakeTransferEvent.NavigateToTransferScreen -> {
                navigateToTransferScreen(event.reviewTransferPayload)
            }
        }
    }

    MakeTransferScreenContent(
        state = state,
        isNetworkAvailable = isNetworkAvailable,
        onAction = {
            viewModel.trySendAction(it)
        },
    )
}

@Composable
internal fun MakeTransferScreenContent(
    state: MakeTransferState,
    isNetworkAvailable: Boolean = false,
    onAction: (MakeTransferAction) -> Unit,
) {
    MifosElevatedScaffold(
        topBarTitle = "Make Transfer",
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
        when (state.dialogState) {
            is MakeTransferState.DialogState.Error -> {
                MifosErrorComponent(
                    message = state.dialogState.message,
                    onRetry = { onAction(MakeTransferAction.OnRetry) },
                    isRetryEnabled = true,
                    isNetworkConnected = isNetworkAvailable,
                )
            }
            MakeTransferState.DialogState.Loading -> {
                MifosProgressIndicator()
            }
            null -> {
                Column(
                    Modifier
                        .fillMaxSize()
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
                        isEnabled = true,
                        labelResId = Res.string.amount,
                        supportingText = "Hello",
                        onClick = { index, _ ->
                            onAction(MakeTransferAction.OnToAccountSelected(state.toAccountOptions[index].accountNo ?: ""))
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
                        label = "Amount",
                        shape = DesignToken.shapes.medium,
                        textStyle = MifosTypography.bodyLarge,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                            errorBorderColor = MaterialTheme.colorScheme.error,
                        ),
                        config = MifosTextFieldConfig(
                            isError = state.amountError.isNotEmpty(),
                            errorText = state.amountError,
                            trailingIcon = if (state.amountError.isNotEmpty()) {
                                {
                                    Icon(
                                        imageVector = MifosIcons.ErrorCircle,
                                        contentDescription = "Error",
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
                        value = state.remarks,
                        onValueChange = { onAction(MakeTransferAction.OnRemarksChanged(it)) },
                        label = "Remarks",
                        shape = DesignToken.shapes.medium,
                        textStyle = MifosTypography.bodyLarge,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                            errorBorderColor = MaterialTheme.colorScheme.error,
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
                                text = "Make Transfer",
                                style = MifosTypography.titleMedium,
                            )
                        },
                        enabled = state.isEnabled,
                        shape = DesignToken.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),
                    )
                }
            }
        }
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
