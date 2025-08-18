/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.application.savingsApplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.savings_application.generated.resources.Res
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_button_continue
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_applicant_name
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_field_officer
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_savings_product
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_submission_date
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_title
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
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosOutlineDropdown
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun SavingsApplyScreen(
    navigateBack: () -> Unit,
    navigateToFillDetailsScreen: (Long, Long, String) -> Unit,
    viewModel: SavingsApplyViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is SavingsApplicationEvent.NavigateBack -> navigateBack.invoke()

            is SavingsApplicationEvent.NavigateToFillDetailsScreen ->
                navigateToFillDetailsScreen.invoke(
                    state.selectedSavingsProductId,
                    state.selectedFieldOfficerId,
                    state.selectedFieldOfficer,
                )
        }
    }

    SavingsAccountContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    SavingsAccountDialog(
        state = state,
        dialogState = state.savingsApplicationDialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun SavingsAccountDialog(
    state: SavingsApplicationState,
    dialogState: SavingsApplicationDialogState?,
    onAction: (SavingsApplicationAction) -> Unit,
) {
    when (dialogState) {
        is SavingsApplicationDialogState.Error -> {
            MifosErrorComponent(
                isRetryEnabled = false,
                message = stringResource(dialogState.message),
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
internal fun SavingsAccountContent(
    state: SavingsApplicationState,
    onAction: (SavingsApplicationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(SavingsApplicationAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_apply_savings_title),
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
        if (state.savingsApplicationDialogState == null) {
            Column(
                modifier = Modifier
                    .padding(DesignToken.padding.large)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
            ) {
                MifosOutlinedTextField(
                    value = state.applicantName,
                    onValueChange = { },
                    label = stringResource(Res.string.feature_apply_savings_label_applicant_name),
                    shape = DesignToken.shapes.medium,
                    textStyle = MifosTypography.bodyLarge,
                    config = MifosTextFieldConfig(
                        enabled = false,
                    ),
                )

                MifosOutlinedTextField(
                    value = state.submittedOnDate,
                    onValueChange = { },
                    label = stringResource(Res.string.feature_apply_savings_label_submission_date),
                    config = MifosTextFieldConfig(
                        showClearIcon = false,
                        enabled = false,
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = MifosIcons.Calendar,
                                contentDescription = "Open Date Picker",
                            )
                        },
                    ),
                    shape = DesignToken.shapes.medium,
                )

                MifosOutlineDropdown(
                    selectedText = state.selectedSavingsProduct,
                    items = state.productOptionsMap,
                    enabled = true,
                    onItemSelected = { id, product ->
                        onAction(SavingsApplicationAction.SavingsProductChange(id, product))
                    },
                    label = stringResource(Res.string.feature_apply_savings_label_savings_product),
                )

                MifosOutlineDropdown(
                    selectedText = state.selectedFieldOfficer,
                    items = state.savingsFieldOfficer,
                    enabled = state.selectedSavingsProduct.isNotBlank(),
                    onItemSelected = { id, officer ->
                        onAction(SavingsApplicationAction.FieldOfficerChange(id, officer))
                    },
                    label = stringResource(Res.string.feature_apply_savings_label_field_officer),
                )

                MifosButton(
                    modifier = Modifier.fillMaxWidth().height(DesignToken.sizes.inputHeight),
                    enabled = state.isFormValid,
                    onClick = {
                        onAction(SavingsApplicationAction.NavigateToConfirmDetails)
                    },
                    shape = DesignToken.shapes.medium,
                ) {
                    Text(
                        text = stringResource(Res.string.feature_apply_savings_button_continue),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}
