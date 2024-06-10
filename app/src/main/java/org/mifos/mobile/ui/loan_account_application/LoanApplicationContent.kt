package org.mifos.mobile.ui.loan_account_application

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosDropDownTextField
import org.mifos.mobile.core.ui.component.MifosOutlinedTextField
import org.mifos.mobile.core.ui.component.MifosTextTitleDescDrawableSingleLine
import org.mifos.mobile.core.ui.component.MifosTextTitleDescSingleLine
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.DateHelper.FORMAT_dd_MM_yyyy
import org.mifos.mobile.utils.PresentOrFutureSelectableDates
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanApplicationContent(
    modifier: Modifier = Modifier,
    uiData: LoanApplicationScreenData,
    selectProduct: (Int) -> Unit,
    selectPurpose: (Int) -> Unit,
    setDisbursementDate: (String) -> Unit,
    reviewClicked: (String) -> Unit,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var purposeTextFieldEnable by rememberSaveable { mutableStateOf(false) }
    var selectedLoanProductError by rememberSaveable { mutableStateOf<String?>(null) }
    var showSelectedLoanProductError by rememberSaveable { mutableStateOf(false) }
    var expectedDisbursementDate by rememberSaveable { mutableStateOf(uiData.disbursementDate) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var selectedLoanProduct by rememberSaveable { mutableStateOf(uiData.selectedLoanProduct) }
    var selectedLoanPurpose by rememberSaveable { mutableStateOf(uiData.selectedLoanPurpose) }
    val datePickerState = rememberDatePickerState(selectableDates = PresentOrFutureSelectableDates)
    var principalAmount by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(uiData.principalAmount ?: ""))
    }
    var principalAmountError by rememberSaveable { mutableStateOf<String?>(null) }
    var showPrincipalAmountError by rememberSaveable { mutableStateOf(false) }


    LaunchedEffect(key1 = uiData) {
        principalAmount = TextFieldValue(uiData.principalAmount ?: "")
    }

    LaunchedEffect(key1 = selectedLoanProduct) {
        principalAmountError = null
        showSelectedLoanProductError = false
        showPrincipalAmountError = false
        selectedLoanProductError = when {
            uiData.selectedLoanProduct.isNullOrBlank() -> context.getString(R.string.select_loan_product_field)
            else -> null
        }
    }

    LaunchedEffect(key1 = principalAmount) {
        showPrincipalAmountError = false
        principalAmountError = when {
            principalAmount.text.isNullOrBlank() -> context.getString(R.string.enter_amount)
            principalAmount.text.matches("^0*".toRegex()) -> context.getString(R.string.amount_greater_than_zero)
            else -> null
        }
    }

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = if (uiData.clientName != null) {
                stringResource(
                    id = R.string.string_and_string,
                    stringResource(id = R.string.new_loan_application) + " ",
                    uiData.clientName ?: "",
                )
            } else {
                stringResource(id = R.string.loan_name)
            },
            color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = stringResource(
                id = R.string.string_and_string,
                stringResource(R.string.account_number) + " ",
                uiData.accountNumber ?: "",
            ),
            color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        MifosDropDownTextField(optionsList = uiData.listLoanProducts.filterNotNull(),
            selectedOption = uiData.selectedLoanProduct,
            supportingText = selectedLoanProductError ?: "",
            error = showSelectedLoanProductError,
            labelResId = R.string.select_loan_product,
            onClick = { position, item ->
                selectProduct(position)
                selectedLoanProduct = item
                purposeTextFieldEnable = true
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosDropDownTextField(optionsList = uiData.listLoanPurpose.filterNotNull(),
            selectedOption = uiData.selectedLoanPurpose,
            isEnabled = purposeTextFieldEnable,
            labelResId = R.string.purpose_of_loan,
            onClick = { index, item ->
                selectPurpose(index)
                selectedLoanPurpose = item
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosOutlinedTextField(
            value = principalAmount,
            onValueChange = { principalAmount = it },
            label = R.string.principal_amount,
            error = showPrincipalAmountError,
            modifier = Modifier.fillMaxWidth(),
            supportingText =  principalAmountError ?: "",
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number,
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosTextTitleDescSingleLine(
            title = stringResource(id = R.string.currency),
            description = uiData.currencyLabel ?: ""
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosTextTitleDescSingleLine(
            title = stringResource(id = R.string.submission_date),
            description = uiData.submittedDate ?: ""
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosTextTitleDescDrawableSingleLine(
            title = stringResource(id = R.string.expected_disbursement_date),
            description = expectedDisbursementDate ?: "",
            imageResId = R.drawable.ic_edit_black_24dp,
            imageSize = 24.dp,
            onDrawableClick = { showDatePicker = true }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                when {
                    selectedLoanProductError != null -> showSelectedLoanProductError = true
                    principalAmountError != null -> showPrincipalAmountError = true
                    else -> reviewClicked(principalAmount.text)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.review))
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            modifier = Modifier.padding(horizontal = 20.dp),
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val formattedDate = DateHelper.getSpecificFormat(
                            format = FORMAT_dd_MM_yyyy,
                            dateLong = datePickerState.selectedDateMillis
                        )
                        formattedDate?.let {
                            expectedDisbursementDate = formattedDate
                            setDisbursementDate(formattedDate)
                        }
                        showDatePicker = false
                    }
                ) { Text(stringResource(id = R.string.dialog_action_ok)) }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) { Text(stringResource(id = R.string.dialog_action_cancel)) }
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        )
        {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    todayContentColor = MaterialTheme.colorScheme.primary,
                    todayDateBorderColor = MaterialTheme.colorScheme.primary,
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoanAccountApplicationContentPreview() {
    MifosMobileTheme {
        LoanApplicationContent(uiData = LoanApplicationScreenData(),
            selectProduct = { },
            selectPurpose = { },
            reviewClicked = { },
            setDisbursementDate = { },
        )
    }
}