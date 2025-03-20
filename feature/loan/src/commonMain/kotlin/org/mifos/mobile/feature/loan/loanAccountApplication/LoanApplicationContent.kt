/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanAccountApplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.account_number
import mifos_mobile.feature.loan.generated.resources.amount_greater_than_zero
import mifos_mobile.feature.loan.generated.resources.currency
import mifos_mobile.feature.loan.generated.resources.dialog_action_cancel
import mifos_mobile.feature.loan.generated.resources.dialog_action_ok
import mifos_mobile.feature.loan.generated.resources.enter_amount
import mifos_mobile.feature.loan.generated.resources.expected_disbursement_date
import mifos_mobile.feature.loan.generated.resources.ic_edit_black_24dp
import mifos_mobile.feature.loan.generated.resources.loan_name
import mifos_mobile.feature.loan.generated.resources.new_loan_application
import mifos_mobile.feature.loan.generated.resources.principal_amount
import mifos_mobile.feature.loan.generated.resources.purpose_of_loan
import mifos_mobile.feature.loan.generated.resources.review
import mifos_mobile.feature.loan.generated.resources.select_loan_product
import mifos_mobile.feature.loan.generated.resources.select_loan_product_field
import mifos_mobile.feature.loan.generated.resources.string_and_string
import mifos_mobile.feature.loan.generated.resources.submission_date
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.common.DateHelper.format
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosTextButton
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosDropDownTextField
import org.mifos.mobile.core.ui.component.MifosTextTitleDescDrawableSingleLine
import org.mifos.mobile.core.ui.component.MifosTextTitleDescSingleLine
import org.mifos.mobile.core.ui.utils.PresentOrFutureSelectableDates

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoanApplicationContent(
    state: LoanApplicationState,
    selectProduct: (Int) -> Unit,
    selectPurpose: (Int) -> Unit,
    setDisbursementDate: (String) -> Unit,
    reviewClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    var purposeTextFieldEnable by rememberSaveable { mutableStateOf(false) }
    var selectedLoanProductError by rememberSaveable { mutableStateOf<String?>(null) }
    var showSelectedLoanProductError by rememberSaveable { mutableStateOf(false) }
    var expectedDisbursementDate by rememberSaveable {
        mutableStateOf(
            state.loanWithAssociations?.timeline?.expectedDisbursementDate,
        )
    }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var selectedLoanProduct by rememberSaveable { mutableStateOf(state.selectedLoanProduct) }
    var selectedLoanPurpose by rememberSaveable { mutableStateOf(state.selectedLoanPurpose) }

    val datePickerState = rememberDatePickerState(selectableDates = PresentOrFutureSelectableDates)
    var principalAmount by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(state.loanWithAssociations?.principal.toString()))
    }

    var principalAmountError by rememberSaveable { mutableStateOf<String?>(null) }
    var showPrincipalAmountError by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = state) {
        state.loanWithAssociations?.timeline?.expectedDisbursementDate?.let {
            expectedDisbursementDate = it
        }
        principalAmount = TextFieldValue(state.principalAmount ?: "")
    }

    LaunchedEffect(key1 = selectedLoanProduct) {
        principalAmountError = null
        showSelectedLoanProductError = false
        showPrincipalAmountError = false
        selectedLoanProductError = when {
            state.selectedLoanProduct.isNullOrBlank() -> getString(Res.string.select_loan_product_field)
            else -> null
        }
    }

    LaunchedEffect(key1 = principalAmount) {
        showPrincipalAmountError = false
        principalAmountError = when {
            principalAmount.text.isBlank() -> getString(Res.string.enter_amount)
            principalAmount.text.matches("^0*".toRegex()) -> getString(Res.string.amount_greater_than_zero)
            else -> null
        }
    }
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = if (state.loanWithAssociations?.clientName != null) {
                stringResource(
                    Res.string.string_and_string,
                    stringResource(Res.string.new_loan_application) + " ",
                    state.loanWithAssociations.clientName.toString(),
                )
            } else {
                stringResource(Res.string.loan_name)
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = stringResource(
                Res.string.string_and_string,
                stringResource(Res.string.account_number) + " ",
                state.loanWithAssociations?.accountNo ?: "",
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        MifosDropDownTextField(
            optionsList = state.listLoanProducts.filterNotNull(),
            selectedOption = state.selectedLoanProduct,
            supportingText = selectedLoanProductError ?: "",
            error = showSelectedLoanProductError,
            labelResId = Res.string.select_loan_product,
            onClick = { position, item ->
                selectProduct(position)
                selectedLoanProduct = item
                purposeTextFieldEnable = true
            },
        )

        MifosDropDownTextField(
            optionsList = state.listLoanPurpose.filterNotNull(),
            selectedOption = state.selectedLoanPurpose,
            isEnabled = purposeTextFieldEnable,
            labelResId = Res.string.purpose_of_loan,
            onClick = { index, item ->
                selectPurpose(index)
                selectedLoanPurpose = item
            },
        )

        MifosOutlinedTextField(
            value = principalAmount.text,
            onValueChange = { principalAmount = TextFieldValue(it) },
            label = stringResource(Res.string.principal_amount),
            modifier = Modifier.fillMaxWidth(),
            config = MifosTextFieldConfig(
                errorText = principalAmountError ?: "",
                isError = showPrincipalAmountError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
            ),
        )

        MifosTextTitleDescSingleLine(
            title = stringResource(Res.string.currency),
            description = state.loanWithAssociations?.currency?.displaySymbol ?: "",
        )

        MifosTextTitleDescSingleLine(
            title = stringResource(Res.string.submission_date),
            description = state.loanWithAssociations?.timeline?.submittedOnDate?.let {
                DateHelper.getDateAsString(
                    it,
                )
            } ?: "",
        )

        MifosTextTitleDescDrawableSingleLine(
            title = stringResource(Res.string.expected_disbursement_date),
            description = expectedDisbursementDate?.let {
                DateHelper.getDateAsString(
                    it,
                )
            } ?: "",
            imageResId = Res.drawable.ic_edit_black_24dp,
            imageSize = 24.dp,
            onDrawableClick = { showDatePicker = true },
        )

        MifosButton(
            modifier = modifier.fillMaxWidth(),
            onClick = {
                when {
                    selectedLoanProductError != null -> showSelectedLoanProductError = true
                    principalAmountError != null -> showPrincipalAmountError = true
                    else -> reviewClicked(principalAmount.text)
                }
            },
            content = {
                Text(
                    text = stringResource(Res.string.review),
                    style = MaterialTheme.typography.titleSmall,
                )
            },
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            modifier = Modifier.padding(horizontal = 20.dp),
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                MifosTextButton(
                    onClick = {
                        val formattedDate = datePickerState.selectedDateMillis?.let { millis ->
                            val instant = Instant.fromEpochMilliseconds(millis)
                            val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                            DateHelper.getSpecificFormat(
                                DateHelper.MONTH_FORMAT,
                                localDate.format(DateHelper.SHORT_MONTH),
                            )
                        }
                        formattedDate?.let {
                            val dateList = DateHelper.getDateAsList(it)
                            expectedDisbursementDate = dateList
                            setDisbursementDate(it)
                        }
                        showDatePicker = false
                    },
                    text = { stringResource(Res.string.dialog_action_ok) },
                )
            },
            dismissButton = {
                MifosTextButton(
                    onClick = { showDatePicker = false },
                    text = { stringResource(Res.string.dialog_action_cancel) },
                )
            },
        ) {
            DatePicker(
                state = datePickerState,
            )
        }
    }
}

@Preview
@Composable
private fun LoanAccountApplicationContentPreview() {
    MifosMobileTheme {
        LoanApplicationContent(
            state = LoanApplicationState(dialogState = null),
            selectProduct = { },
            selectPurpose = { },
            reviewClicked = { },
            setDisbursementDate = { },
        )
    }
}
