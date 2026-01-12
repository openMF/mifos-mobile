/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.loanApplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.loan_application.generated.resources.Res
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_button_continue
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_label_applicant_name
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_label_disbursement_date
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_label_loan_product
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_label_principal_amount
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_label_purpose
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_section_fill_details
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_unsaved_changes_message
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_unsaved_changes_title
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
import org.mifos.mobile.core.ui.utils.ScreenUiState
import template.core.base.designsystem.theme.KptTheme
import kotlin.time.ExperimentalTime

/**
 * Entry point for the Loan Application form.
 * Manages the form state, handles navigation events, and coordinates dialog interactions.
 *
 * @param navigateBack Callback to return to the previous screen.
 * @param navigateToConfirmDetailsScreen Callback to proceed to the confirmation
 * page with valid form data.
 * @param viewModel The state holder managing form validation and business logic.
 */
@Composable
internal fun LoanApplyScreen(
    navigateBack: () -> Unit,
    navigateToConfirmDetailsScreen: (Long, String, String, String, String, String) -> Unit,
    viewModel: LoanApplyViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is LoanApplicationEvent.NavigateBack -> navigateBack.invoke()

            is LoanApplicationEvent.NavigateToConfirmDetailsScreen -> {
                navigateToConfirmDetailsScreen(
                    state.loanProductId.toLong(),
                    state.applicantName,
                    state.loanProductName,
                    state.selectedLoanPurpose,
                    state.disbursementDate,
                    state.principalAmount,
                )
            }
        }
    }

    LoanAccountContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    LoanAccountDialog(
        dialogState = state.loanApplicationDialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

/**
 * Renders modal dialogs for critical interruptions, such as error alerts or unsaved changes warnings.
 *
 * @param dialogState The current dialog type to display (Error or UnsavedChanges).
 * @param onAction Callback to handle dialog responses (e.g., dismiss, confirm navigation).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoanAccountDialog(
    dialogState: LoanApplicationDialogState?,
    onAction: (LoanApplicationAction) -> Unit,
) {
    when (dialogState) {
        is LoanApplicationDialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = stringResource(dialogState.message),
                ),
                onDismissRequest = { onAction(LoanApplicationAction.ConfirmNavigation) },
            )
        }

        is LoanApplicationDialogState.UnsavedChanges -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    title = stringResource(Res.string.feature_apply_loan_unsaved_changes_title),
                    message = stringResource(Res.string.feature_apply_loan_unsaved_changes_message),
                ),
                onDismissRequest = { onAction(LoanApplicationAction.DismissDialog) },
                onConfirm = { onAction(LoanApplicationAction.ConfirmNavigation) },
            )
        }

        null -> {}
    }
}

/**
 * Displays the input fields for the loan application, including name, purpose, and disbursement date.
 * Handles the date picker visibility and input validation feedback.
 *
 * @param state The current UI state containing field values, errors, and list options.
 * @param onAction Callback for user interactions (text input, date selection, submission).
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
internal fun LoanAccountContent(
    state: LoanApplicationState,
    onAction: (LoanApplicationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(LoanApplicationAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_apply_loan_section_fill_details),
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
            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = stringResource(state.uiState.message),
                    onRetry = { onAction(LoanApplicationAction.Retry) },
                )
            }

            ScreenUiState.Loading -> MifosProgressIndicator()

            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(LoanApplicationAction.Retry) },
                )
            }

            ScreenUiState.Success -> {
                Column(
                    modifier = Modifier
                        .padding(KptTheme.spacing.md)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.md),
                ) {
                    MifosOutlinedTextField(
                        value = state.applicantName,
                        onValueChange = { },
                        label = stringResource(Res.string.feature_apply_loan_label_applicant_name),
                        shape = KptTheme.shapes.medium,
                        textStyle = MifosTypography.bodyLarge,
                        config = MifosTextFieldConfig(
                            enabled = false,
                        ),
                    )

                    MifosOutlinedTextField(
                        value = state.disbursementDate,
                        onValueChange = { },
                        label = stringResource(Res.string.feature_apply_loan_label_disbursement_date),
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
                        shape = KptTheme.shapes.medium,
                    )

                    MifosOutlineDropdown(
                        selectedText = state.loanProductName,
                        items = emptyMap(),
                        enabled = false,
                        onItemSelected = { _, _ -> },
                        label = stringResource(Res.string.feature_apply_loan_label_loan_product),
                    )

                    MifosOutlineDropdown(
                        selectedText = state.selectedLoanPurpose,
                        items = state.loanPurposeOptions,
                        onItemSelected = { id, product ->
                            onAction(LoanApplicationAction.PurposeOfLoanChange(product))
                        },
                        label = stringResource(Res.string.feature_apply_loan_label_purpose),
                    )

                    MifosOutlinedTextField(
                        value = state.principalAmount,
                        onValueChange = { onAction(LoanApplicationAction.PrincipalAmountChange(it)) },
                        label = stringResource(Res.string.feature_apply_loan_label_principal_amount),
                        config = MifosTextFieldConfig(
                            isError = state.principalAmountError != null,
                            errorText = state.principalAmountError?.let { stringResource(it) },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done,
                            ),
                        ),
                        shape = KptTheme.shapes.medium,
                    )

                    MifosButton(
                        modifier = Modifier.fillMaxWidth().height(DesignToken.sizes.inputHeight),
                        enabled = state.isFormValid,
                        onClick = {
                            onAction(LoanApplicationAction.NavigateToConfirmDetails)
                        },
                        shape = KptTheme.shapes.medium,
                    ) {
                        Text(
                            text = stringResource(Res.string.feature_apply_loan_button_continue),
                            style = KptTheme.typography.labelLarge,
                        )
                    }
                }
            }

            else -> { }
        }
    }
}
