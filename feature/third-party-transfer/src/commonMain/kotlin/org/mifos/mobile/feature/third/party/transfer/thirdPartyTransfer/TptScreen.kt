/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.third.party.transfer.thirdPartyTransfer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.core.ui.generated.resources.ic_icon_logo_1
import mifos_mobile.feature.third_party_transfer.generated.resources.Res
import mifos_mobile.feature.third_party_transfer.generated.resources.feature_tpt_error_server
import mifos_mobile.feature.third_party_transfer.generated.resources.feature_tpt_label_amount
import mifos_mobile.feature.third_party_transfer.generated.resources.feature_tpt_label_destination
import mifos_mobile.feature.third_party_transfer.generated.resources.feature_tpt_label_origin_account
import mifos_mobile.feature.third_party_transfer.generated.resources.feature_tpt_label_remarks
import mifos_mobile.feature.third_party_transfer.generated.resources.feature_tpt_tip
import mifos_mobile.feature.third_party_transfer.generated.resources.feature_tpt_tip_action
import mifos_mobile.feature.third_party_transfer.generated.resources.feature_tpt_transfer_button
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosDropDownDoubleTextField
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPayFromDropdownUI
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.third.party.transfer.navigation.TptNavigationDestination
import org.mifos.mobile.feature.third.party.transfer.navigation.TptNavigator

@Composable
internal fun TptScreen(
    onNavigate: TptNavigator,
    viewModel: TptViewModel = koinViewModel(),
) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is TptEvent.NavigateToTransferScreen -> {
                onNavigate(TptNavigationDestination.TransferProcess(event.reviewTransferPayload))
            }

            is TptEvent.NavigateToNotificationScreen -> {
                onNavigate(TptNavigationDestination.Notification)
            }

            is TptEvent.NavigateToAddBeneficiaryScreen -> {
                onNavigate(TptNavigationDestination.AddBeneficiaryScreen)
            }
        }
    }

    TprContent(
        state = uiState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    TptDialog(
        dialogState = uiState.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun TptDialog(
    dialogState: TptState.DialogState?,
    onAction: (TptAction) -> Unit,
) {
    when (dialogState) {
        is TptState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = stringResource(dialogState.message),
                ),
                onDismissRequest = { onAction(TptAction.DismissDialog) },
            )
        }

        null -> {}
    }
}

@Composable
internal fun TprContent(
    state: TptState,
    onAction: (TptAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        modifier = modifier,
        brandIcon = mifos_mobile.core.ui.generated.resources.Res.drawable.ic_icon_logo_1,
        topBarTitle = "Home",
        onNavigateBack = { },
        actions = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
            ) {
                // TODO : once ui/ux team gives this flow uncomment and implement
//                Image(
//                    imageVector = MifosIcons.SearchNew,
//                    contentDescription = null,
//                )
                Image(
                    imageVector = MifosIcons.Alert,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        onAction(TptAction.OnNotificationClicked)
                    },
                )
            }
        },
    ) {
        when (state.uiState) {
            TptState.TptScreenState.Loading -> MifosProgressIndicator()

            TptState.TptScreenState.OverlayLoading -> MifosProgressIndicatorOverlay()

            is TptState.TptScreenState.Error -> {
                MifosErrorComponent(
                    message = stringResource(Res.string.feature_tpt_error_server),
                    isRetryEnabled = true,
                    onRetry = { onAction(TptAction.OnRetry) },
                )
            }

            TptState.TptScreenState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(TptAction.OnRetry) },
                )
            }

            TptState.TptScreenState.Success -> {
                TptForm(state, onAction)
            }

            null -> { }
        }
    }
}

@Composable
internal fun TptForm(
    state: TptState,
    onAction: (TptAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(DesignToken.padding.large)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(DesignToken.padding.largeIncreased),
    ) {
        MifosPayFromDropdownUI(
            accounts = state.fromAccountOptions.map
                { Pair(it.accountNo ?: "", it.clientName ?: "") },
            onAccountSelected = { account, balance ->
                onAction(TptAction.OnFromAccountSelected(account))
            },
            label = stringResource(Res.string.feature_tpt_label_origin_account),
        )

        MifosDropDownDoubleTextField(
            optionsList = state.toAccountOptions.map
                { Pair(it.accountNo ?: "", it.clientName ?: "") },
            selectedOption = state.toAccount?.accountNo ?: "",
            isEnabled = true,
            labelResId = Res.string.feature_tpt_label_destination,
            onClick = { index, _ ->
                onAction(
                    TptAction.OnToAccountSelected(
                        state.toAccountOptions[index].accountNo ?: "",
                    ),
                )
            },
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.small),
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        ) {
            Text(
                text = stringResource(Res.string.feature_tpt_tip),
                style = MifosTypography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
            )

            Text(
                modifier = Modifier
                    .clickable {
                        onAction(TptAction.OnAddBeneficiaryClicked)
                    },
                text = stringResource(Res.string.feature_tpt_tip_action),
                style = MifosTypography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        MifosOutlinedTextField(
            value = state.amount,
            onValueChange = { onAction(TptAction.OnAmountChanged(it)) },
            label = stringResource(Res.string.feature_tpt_label_amount),
            shape = DesignToken.shapes.medium,
            textStyle = MifosTypography.bodyLarge,
            config = MifosTextFieldConfig(
                isError = state.amountError != null,
                errorText = state.amountError?.let {
                    stringResource(it)
                },
                trailingIcon = if (state.amountError != null) {
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            ),
        )

        MifosOutlinedTextField(
            value = state.remark,
            onValueChange = { onAction(TptAction.OnRemarksChanged(it)) },
            label = stringResource(Res.string.feature_tpt_label_remarks),
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
                onAction(TptAction.OnMakeTransferClicked)
            },
            enabled = state.isEnabled,
            shape = DesignToken.shapes.medium,
        ) {
            Text(
                text = stringResource(Res.string.feature_tpt_transfer_button),
                style = MifosTypography.titleMedium,
            )
        }
    }
}
