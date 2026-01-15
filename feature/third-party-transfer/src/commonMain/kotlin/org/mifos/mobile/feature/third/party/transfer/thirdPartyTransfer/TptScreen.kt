/*
 * Copyright 2026 Mifos Initiative
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
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
import org.mifos.mobile.core.common.formatAmount
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.ui.component.MifosDropDownDoubleTextField
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPayFromDropdownUI
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.third.party.transfer.navigation.TptNavigationDestination
import org.mifos.mobile.feature.third.party.transfer.navigation.TptNavigator
import template.core.base.designsystem.theme.KptTheme

/**
 * Composable function for the Third Party Transfer screen.
 *
 * @param onNavigate A [TptNavigator] function to handle navigation events from this screen.
 * @param viewModel The view model for the Third Party Transfer screen, defaulting to a Koin view model.
 * */
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

/**
* Composable function for the Third Party Transfer dialog.
*
* @param dialogState The dialog state for the Third Party Transfer screen.
* @param onAction A [TptAction] function to handle actions from this dialog.
* */
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

/**
 * Composable function for the main content of the Third Party Transfer screen.
 * It handles displaying the loading indicator, error messages, and the main form.
 *
 * @param state The current state of the Third Party Transfer screen.
 * @param onAction A function to handle actions from this composable.
 * @param modifier The modifier to apply to this composable.
 */
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
                horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.md),
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
                    colorFilter = ColorFilter.tint(KptTheme.colorScheme.onSurface),
                )
            }
        },
    ) {
        when (state.uiState) {
            ScreenUiState.Loading -> MifosProgressIndicator()

            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    message = stringResource(Res.string.feature_tpt_error_server),
                    isRetryEnabled = true,
                    onRetry = { onAction(TptAction.OnRetry) },
                )
            }

            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(TptAction.OnRetry) },
                )
            }

            ScreenUiState.Success -> {
                TptForm(state, onAction)
            }

            else -> { }
        }
    }
}

/**
* Composable function for the Third Party Transfer form.
*
* @param state The state for the Third Party Transfer screen.
* @param onAction A [TptAction] function to handle actions from this form.
* @param modifier The modifier to apply to this composable.
* */
@Composable
internal fun TptForm(
    state: TptState,
    onAction: (TptAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(KptTheme.spacing.md)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.md),
    ) {
        MifosPayFromDropdownUI(
            accounts = state.fromAccountOptions.map
                { Pair(it.accountNo ?: "", it.clientName ?: "") },
            onAccountSelected = { accountNo, _ ->

                val selectedAccount = state.fromAccountOptions
                    .firstOrNull { it.accountNo == accountNo }

                if (selectedAccount == null) {
                    return@MifosPayFromDropdownUI
                }

                val accountId = selectedAccount.accountId

                if (accountId == null) {
                    return@MifosPayFromDropdownUI
                }

                onAction(
                    TptAction.OnFromAccountSelected(
                        accountId = accountId.toLong(),
                        accountNo = accountNo,
                    ),
                )
            },
            label = stringResource(Res.string.feature_tpt_label_origin_account),
            selectedAccountNo = state.fromAccount?.accountNo ?: "",
            selectedAccountName = state.fromAccount?.clientName ?: "",
            showExtendedDetails = true,
            productName = state.fromAccountDetails?.savingsProductName,
            availableBalance = state.fromAccountBalance?.let { formatAmount(it) },
            isBalanceLoading = state.isBalanceLoading,
            balanceError = state.balanceError,
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
            horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm),
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        ) {
            Text(
                text = stringResource(Res.string.feature_tpt_tip),
                style = KptTheme.typography.labelMedium,
                color = KptTheme.colorScheme.secondary,
            )

            Text(
                modifier = Modifier
                    .clickable {
                        onAction(TptAction.OnAddBeneficiaryClicked)
                    },
                text = stringResource(Res.string.feature_tpt_tip_action),
                style = KptTheme.typography.labelMedium,
                color = KptTheme.colorScheme.primary,
            )
        }

        MifosOutlinedTextField(
            value = state.amount,
            onValueChange = { onAction(TptAction.OnAmountChanged(it)) },
            label = stringResource(Res.string.feature_tpt_label_amount),
            shape = KptTheme.shapes.medium,
            textStyle = KptTheme.typography.bodyLarge,
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
                            tint = KptTheme.colorScheme.error,
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
            shape = KptTheme.shapes.medium,
            textStyle = KptTheme.typography.bodyLarge,
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
                            tint = KptTheme.colorScheme.error,
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
            shape = KptTheme.shapes.medium,
        ) {
            Text(
                text = stringResource(Res.string.feature_tpt_transfer_button),
                style = KptTheme.typography.titleMedium,
            )
        }
    }
}
