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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.Clock
import mifos_mobile.feature.loan_application.generated.resources.Res
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_button_cancel
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_button_continue
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_button_ok
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_label_applicant_name
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_label_disbursement_date
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_label_loan_product
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_label_principal_amount
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_label_purpose
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_section_fill_details
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.DateHelper
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
        state = state,
        dialogState = state.loanApplicationDialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoanAccountDialog(
    state: LoanApplicationState,
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

        LoanApplicationDialogState.Loading -> MifosProgressIndicator()

        LoanApplicationDialogState.OverlayLoading -> MifosProgressIndicator()

        is LoanApplicationDialogState.ShowDatePicker -> {
            val today = Clock.System.now().toEpochMilliseconds()
            val activationMillis = DateHelper.getDateAsLongFromList(
                DateHelper.getDateAsList(state.activationDate),
            ) ?: 0L

            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = state.currentDate,
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return utcTimeMillis in activationMillis..today
                    }
                },
            )

            DatePickerDialog(
                onDismissRequest = {
                    onAction(LoanApplicationAction.ToggleDatePicker)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onAction(LoanApplicationAction.ToggleDatePicker)
                            datePickerState.selectedDateMillis?.let {
                                onAction(
                                    LoanApplicationAction.DisbursementDateChange(
                                        DateHelper.getDateMonthYearString(it),
                                    ),
                                )
                            }
                        },
                    ) {
                        Text(stringResource(Res.string.feature_apply_loan_button_ok))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onAction(LoanApplicationAction.ToggleDatePicker)
                        },
                    ) {
                        Text(stringResource(Res.string.feature_apply_loan_button_cancel))
                    }
                },
            ) {
                DatePicker(state = datePickerState)
            }
        }

        is LoanApplicationDialogState.UnsavedChanges -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = stringResource(dialogState.message),
                ),
                onDismissRequest = { onAction(LoanApplicationAction.DismissDialog) },
                onConfirm = { onAction(LoanApplicationAction.ConfirmNavigation) },
            )
        }

        is LoanApplicationDialogState.Network -> {
            MifosErrorComponent(
                isNetworkConnected = state.networkStatus,
                isRetryEnabled = true,
                onRetry = { onAction(LoanApplicationAction.Retry) },
            )
        }

        null -> {}
    }
}

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
        if (state.loanApplicationDialogState == null) {
            Column(
                modifier = Modifier
                    .padding(DesignToken.padding.large)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
            ) {
                MifosOutlinedTextField(
                    value = state.applicantName,
                    onValueChange = { onAction(LoanApplicationAction.ApplicantNameChange(it)) },
                    label = stringResource(Res.string.feature_apply_loan_label_applicant_name),
                    shape = DesignToken.shapes.medium,
                    textStyle = MifosTypography.bodyLarge,
                    config = MifosTextFieldConfig(
                        isError = state.applicantNameError != null,
                        errorText = state.applicantNameError?.let { stringResource(it) },
                    ),
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
                    value = state.disbursementDate,
                    onValueChange = { },
                    label = stringResource(Res.string.feature_apply_loan_label_disbursement_date),
                    config = MifosTextFieldConfig(
                        isError = state.disbursementDateError != null,
                        errorText = state.disbursementDateError?.let { stringResource(it) },
                        showClearIcon = false,
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.clickable {
                                    onAction(LoanApplicationAction.ToggleDatePicker)
                                },
                                imageVector = MifosIcons.Calendar,
                                contentDescription = "Open Date Picker",
                            )
                        },
                    ),
                    shape = DesignToken.shapes.medium,
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
                    shape = DesignToken.shapes.medium,
                )

                MifosButton(
                    modifier = Modifier.fillMaxWidth().height(DesignToken.sizes.inputHeight),
                    enabled = state.isFormValid,
                    onClick = {
                        onAction(LoanApplicationAction.NavigateToConfirmDetails)
                    },
                    shape = DesignToken.shapes.medium,
                ) {
                    Text(
                        text = stringResource(Res.string.feature_apply_loan_button_continue),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}
