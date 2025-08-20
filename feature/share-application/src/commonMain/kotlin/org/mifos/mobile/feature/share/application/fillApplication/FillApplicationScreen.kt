/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.share.application.fillApplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.share_application.generated.resources.Res
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_error_server
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_label_currency
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_label_current_price
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_label_lockin_frequency
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_label_lockin_frequency_type
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_label_minimum_frequency
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_label_terms
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_label_total_number_of_shares
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_section_lockin_period
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_section_minimum_active_period
import mifos_mobile.feature.share_application.generated.resources.feature_share_button_next
import mifos_mobile.feature.share_application.generated.resources.feature_share_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosOutlineDropdown
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun ShareFillApplicationScreen(
    navigateBack: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    viewModel: ShareFillApplicationViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is ShareApplicationEvent.NavigateBack -> navigateBack.invoke()

            ShareApplicationEvent.NavigateToAuthentication -> navigateToAuthenticateScreen.invoke()

            is ShareApplicationEvent.NavigateToStatus -> {
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

    ShareFillApplicationContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    ShareFillApplicationDialog(
        dialogState = state.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun ShareFillApplicationDialog(
    dialogState: ShareApplicationDialogState?,
    onAction: (ShareApplicationAction) -> Unit,
) {
    when (dialogState) {
        is ShareApplicationDialogState.Error -> {
            MifosErrorComponent(
                message = stringResource(dialogState.message),
            )
        }

        is ShareApplicationDialogState.UnsavedChanges -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = stringResource(dialogState.message),
                ),
                onDismissRequest = { onAction(ShareApplicationAction.DismissDialog) },
                onConfirm = { onAction(ShareApplicationAction.ConfirmNavigation) },
            )
        }

        null -> {}
    }
}

@Composable
internal fun ShareFillApplicationContent(
    state: ShareApplicationState,
    onAction: (ShareApplicationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(ShareApplicationAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_share_title),
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
        when (state.uiState) {
            is ShareApplicationUiState.Loading -> {
                MifosProgressIndicator()
            }

            is ShareApplicationUiState.OverlayLoading -> {
                MifosProgressIndicatorOverlay()
            }

            is ShareApplicationUiState.Error -> {
                MifosErrorComponent(
                    message = stringResource(Res.string.feature_apply_share_error_server),
                    isRetryEnabled = true,
                    onRetry = { onAction(ShareApplicationAction.Retry) },
                )
            }

            is ShareApplicationUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(ShareApplicationAction.Retry) },
                )
            }

            is ShareApplicationUiState.Success -> {
                ShareFillApplicationForm(state, onAction, modifier)
            }

            null -> {}
        }
    }
}

@Composable
internal fun ShareFillApplicationForm(
    state: ShareApplicationState,
    onAction: (ShareApplicationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(DesignToken.padding.large)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
    ) {
        Text(
            text = stringResource(Res.string.feature_apply_share_label_terms),
            style = MifosTypography.labelLargeEmphasized,
            color = AppColors.customBlack,
        )

        MifosOutlineDropdown(
            selectedText = state.currency.displayLabel ?: "",
            items = emptyMap(),
            enabled = false,
            onItemSelected = { _, _ -> },
            label = stringResource(Res.string.feature_apply_share_label_currency),
        )

        MifosOutlinedTextField(
            value = state.currentPrice,
            onValueChange = { onAction(ShareApplicationAction.CurrentPriceChange(it)) },
            label = stringResource(Res.string.feature_apply_share_label_current_price),
            shape = DesignToken.shapes.medium,
            textStyle = MifosTypography.bodyLarge,
            config = MifosTextFieldConfig(
                isError = state.currentPriceError != null,
                errorText = state.currentPriceError?.let { stringResource(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
            ),
        )

        MifosOutlinedTextField(
            value = state.totalNumberOfShares,
            onValueChange = { onAction(ShareApplicationAction.CurrentPriceChange(it)) },
            label = stringResource(Res.string.feature_apply_share_label_total_number_of_shares),
            shape = DesignToken.shapes.medium,
            textStyle = MifosTypography.bodyLarge,
            config = MifosTextFieldConfig(
                isError = state.sharesError != null,
                errorText = state.sharesError?.let { stringResource(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
            ),
        )

        MifosOutlineDropdown(
            selectedText = state.defaultSavingsAccountName,
            items = state.accountIdNameMap,
            enabled = false,
            onItemSelected = { id, value ->
                onAction(
                    ShareApplicationAction.DefaultSavingsAccountChange
                        (id, value),
                )
            },
            label = stringResource(Res.string.feature_apply_share_label_currency),
        )

        Text(
            text = stringResource(Res.string.feature_apply_share_section_minimum_active_period),
            style = MifosTypography.labelLargeEmphasized,
            color = AppColors.customBlack,
        )

        MifosOutlinedTextField(
            value = state.mapFrequency,
            onValueChange = { onAction(ShareApplicationAction.MapFrequencyChange(it)) },
            label = stringResource(Res.string.feature_apply_share_label_minimum_frequency),
            shape = DesignToken.shapes.medium,
            textStyle = MifosTypography.bodyLarge,
            config = MifosTextFieldConfig(
                isError = state.mapFrequencyError != null,
                errorText = state.mapFrequencyError?.let { stringResource(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
            ),
        )

        MifosOutlineDropdown(
            selectedText = state.selectedMapFrequencyTypeName,
            items = state.mapFrequencyMap,
            enabled = false,
            onItemSelected = { id, value ->
                onAction(ShareApplicationAction.MapFrequencyTypeChange(id, value))
            },
            label = stringResource(Res.string.feature_apply_share_label_currency),
        )

        Text(
            text = stringResource(Res.string.feature_apply_share_section_lockin_period),
            style = MifosTypography.labelLargeEmphasized,
            color = AppColors.customBlack,
        )

        MifosOutlinedTextField(
            value = state.lipFrequency,
            onValueChange = { onAction(ShareApplicationAction.MapFrequencyChange(it)) },
            label = stringResource(Res.string.feature_apply_share_label_lockin_frequency),
            shape = DesignToken.shapes.medium,
            textStyle = MifosTypography.bodyLarge,
            config = MifosTextFieldConfig(
                isError = state.lipFrequencyError != null,
                errorText = state.lipFrequencyError?.let { stringResource(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
            ),
        )

        MifosOutlineDropdown(
            selectedText = state.selectedLipFrequencyTypeName,
            items = state.lipFrequencyMap,
            onItemSelected = { id, product ->
                onAction(ShareApplicationAction.LipFrequencyTypeChange(id, product))
            },
            label = stringResource(Res.string.feature_apply_share_label_lockin_frequency_type),
        )

        MifosButton(
            modifier = Modifier.fillMaxWidth().height(DesignToken.sizes.inputHeight),
            enabled = state.isFormValid,
            onClick = {
                onAction(ShareApplicationAction.NavigateToAuthentication)
            },
            shape = DesignToken.shapes.medium,
        ) {
            Text(
                text = stringResource(Res.string.feature_share_button_next),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Preview
@Composable
private fun Share_Application_Success_Preview() {
    MifosMobileTheme {
        ShareFillApplicationContent(
            state = ShareApplicationState(
                uiState = ShareApplicationUiState.Success,
                clientId = 1L,
                shareProductId = 1L,
            ),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun Share_Application_Error_Preview() {
    MifosMobileTheme {
        ShareFillApplicationContent(
            state = ShareApplicationState(
                uiState = ShareApplicationUiState.Error(Res.string.feature_apply_share_error_server),
                clientId = 1L,
                shareProductId = 1L,
            ),
            onAction = {},
        )
    }
}
