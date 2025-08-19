/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.application.fillApplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.savings_application.generated.resources.Res
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_currency
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_details
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_frequency
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_frequency_type
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_lock_in_period
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_minimum_opening_balance
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_overdraft
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_request_to_allow_overdraft
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_section_fill_details
import mifos_mobile.feature.savings_application.generated.resources.feature_button_next
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.designsystem.utils.onClick
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosOutlineDropdown
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun SavingsFillApplicationScreen(
    navigateBack: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    viewModel: SavingsFillApplicationViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is SavingsApplicationEvent.NavigateBack -> navigateBack.invoke()

            SavingsApplicationEvent.NavigateToAuthentication -> navigateToAuthenticateScreen.invoke()

            is SavingsApplicationEvent.NavigateToStatus -> {
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

    SavingsFillApplicationContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    SavingsFillApplicationDialog(
        state = state,
        dialogState = state.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun SavingsFillApplicationDialog(
    state: SavingsApplicationState,
    dialogState: SavingsApplicationDialogState?,
    onAction: (SavingsApplicationAction) -> Unit,
) {
    when (dialogState) {
        is SavingsApplicationDialogState.Error -> {
            MifosErrorComponent(
                message = stringResource(dialogState.message),
                isRetryEnabled = true,
                isNetworkConnected = state.networkStatus,
            )
        }

        SavingsApplicationDialogState.Loading -> MifosProgressIndicator()

        SavingsApplicationDialogState.OverlayLoading -> MifosProgressIndicator()

        is SavingsApplicationDialogState.UnsavedChanges -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = stringResource(dialogState.message),
                ),
                onDismissRequest = { onAction(SavingsApplicationAction.DismissDialog) },
                onConfirm = { onAction(SavingsApplicationAction.ConfirmNavigation) },
            )
        }

        is SavingsApplicationDialogState.Network -> {
            MifosErrorComponent(
                isNetworkConnected = state.networkStatus,
                isRetryEnabled = true,
                onRetry = { onAction(SavingsApplicationAction.Retry) },
            )
        }

        null -> {}
    }
}

@Composable
internal fun SavingsFillApplicationContent(
    state: SavingsApplicationState,
    onAction: (SavingsApplicationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(SavingsApplicationAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_apply_savings_section_fill_details),
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
    ) {
        if (state.dialogState == null) {
            Column(
                modifier = Modifier
                    .padding(DesignToken.padding.large)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
            ) {
                Text(
                    text = stringResource(Res.string.feature_apply_savings_label_details),
                    style = MifosTypography.labelLargeEmphasized,
                    color = AppColors.customBlack,
                )

                MifosOutlineDropdown(
                    selectedText = state.currency.displayLabel ?: "",
                    items = emptyMap(),
                    enabled = false,
                    onItemSelected = { _, _ -> },
                    label = stringResource(Res.string.feature_apply_savings_label_currency),
                )

                MifosOutlinedTextField(
                    value = state.minOpeningBalance,
                    onValueChange = { onAction(SavingsApplicationAction.MinimumOpeningBalanceChange(it)) },
                    label = stringResource(Res.string.feature_apply_savings_label_minimum_opening_balance),
                    shape = DesignToken.shapes.medium,
                    textStyle = MifosTypography.bodyLarge,
                    config = MifosTextFieldConfig(
                        isError = state.minOpeningBalanceError != null,
                        errorText = state.minOpeningBalanceError?.let { stringResource(it) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done,
                        ),
                    ),
                )

                Text(
                    text = stringResource(Res.string.feature_apply_savings_label_lock_in_period),
                    style = MifosTypography.labelLargeEmphasized,
                    color = AppColors.customBlack,
                )

                MifosOutlinedTextField(
                    value = state.frequency,
                    onValueChange = { onAction(SavingsApplicationAction.FrequencyChange(it)) },
                    label = stringResource(Res.string.feature_apply_savings_label_frequency),
                    shape = DesignToken.shapes.medium,
                    textStyle = MifosTypography.bodyLarge,
                    config = MifosTextFieldConfig(
                        isError = state.frequencyError != null,
                        errorText = state.frequencyError?.let { stringResource(it) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done,
                        ),
                    ),
                )

                MifosOutlineDropdown(
                    selectedText = state.selectedFrequencyTypeName,
                    items = state.getFrequencyTypeMap,
                    onItemSelected = { id, product ->
                        onAction(SavingsApplicationAction.FrequencyTypeChange(id, product))
                    },
                    label = stringResource(Res.string.feature_apply_savings_label_frequency_type),
                )

                if (state.isOverDraftAllowed) {
                    Text(
                        text = stringResource(Res.string.feature_apply_savings_label_overdraft),
                        style = MifosTypography.labelLargeEmphasized,
                        color = AppColors.customBlack,
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onClick { onAction(SavingsApplicationAction.OnChecked(!state.checked)) },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = state.checked,
                            onCheckedChange = {
                                onAction(SavingsApplicationAction.OnChecked(it))
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = AppColors.primaryBlue,
                            ),
                        )
                        Text(
                            text = stringResource(Res.string.feature_apply_savings_label_request_to_allow_overdraft),
                            style = MifosTypography.labelMediumEmphasized,
                        )
                    }
                }

                MifosButton(
                    modifier = Modifier.fillMaxWidth().height(DesignToken.sizes.inputHeight),
                    enabled = state.isFormValid,
                    onClick = {
                        onAction(SavingsApplicationAction.NavigateToAuthentication)
                    },
                    shape = DesignToken.shapes.medium,
                ) {
                    Text(
                        text = stringResource(Res.string.feature_button_next),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}
