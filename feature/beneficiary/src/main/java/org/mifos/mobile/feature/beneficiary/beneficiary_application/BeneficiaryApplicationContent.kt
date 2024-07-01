package org.mifos.mobile.feature.beneficiary.beneficiary_application

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.component.MifosDropDownTextField
import org.mifos.mobile.core.ui.component.MifosOutlinedTextField
import org.mifos.mobile.core.ui.component.MifosTextButton
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.beneficiary.R

@Composable
fun BeneficiaryApplicationContent(
    prefilledBeneficiary: Beneficiary?,
    beneficiaryTemplate: BeneficiaryTemplate,
    beneficiaryState: BeneficiaryState,
    onSubmit: (BeneficiaryPayload) -> Unit
) {
    val scrollState = rememberScrollState()

    var accountType by rememberSaveable { mutableIntStateOf(prefilledBeneficiary?.accountType?.id ?: -1) }
    var accountTypeError by rememberSaveable { mutableStateOf<Int?>(null) }

    var accountNumber by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(prefilledBeneficiary?.accountNumber ?: "")) }
    var accountNumberError by rememberSaveable { mutableStateOf<Int?>(null) }

    var officeName by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(prefilledBeneficiary?.officeName ?: "")) }
    var officeNameError by rememberSaveable { mutableStateOf<Int?>(null) }

    var transferLimit by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(prefilledBeneficiary?.transferLimit?.toString() ?: "")) }
    var transferLimitError by rememberSaveable { mutableStateOf<Int?>(null) }

    var beneficiaryName by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(prefilledBeneficiary?.name ?: "")) }
    var beneficiaryNameError by rememberSaveable { mutableStateOf<Int?>(null) }

    fun validateFields() {
        if (beneficiaryState != BeneficiaryState.UPDATE) {
            accountTypeError = if (accountType == -1) R.string.select_account_type else null
            accountNumberError = if (accountNumber.text.trim().isEmpty()) R.string.enter_account_number else null
            officeNameError = if (officeName.text.trim().isEmpty()) R.string.enter_office_name else null
        }
        transferLimitError = when {
            transferLimit.text.trim().isEmpty() -> R.string.enter_transfer_limit
            transferLimit.text.any { it.isLetter() } -> R.string.invalid_amount
            transferLimit.text.toDoubleOrNull()?.let { it % 1 != 0.0 } == true -> R.string.invalid_amount
            else -> null
        }
        beneficiaryNameError = if (beneficiaryName.text.trim().isEmpty()) R.string.enter_beneficiary_name else null
    }

    LaunchedEffect(key1 = accountType) {
        accountTypeError = null
    }

    LaunchedEffect(key1 = accountNumber) {
        accountNumberError = null
    }

    LaunchedEffect(key1 = officeName) {
        officeNameError = null
    }

    LaunchedEffect(key1 = transferLimit) {
        transferLimitError = null
    }

    LaunchedEffect(key1 = beneficiaryName) {
        beneficiaryNameError = null
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
        MifosDropDownTextField(
            optionsList = beneficiaryTemplate.accountTypeOptions?.mapNotNull { it.value } ?: listOf(),
            labelResId = R.string.select_account_type,
            onClick = { index, _ -> accountType = beneficiaryTemplate.accountTypeOptions?.filter{ it.value != null }?.get(index)?.id ?: -1 },
            error = accountTypeError != null && beneficiaryState != BeneficiaryState.UPDATE,
            isEnabled = beneficiaryState != BeneficiaryState.UPDATE,
            supportingText = accountTypeError?.let { stringResource(id = it) }
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = accountNumber,
            onValueChange = { accountNumber = it },
            label = R.string.account_number,
            error = accountNumberError != null && beneficiaryState != BeneficiaryState.UPDATE,
            enabled = beneficiaryState != BeneficiaryState.UPDATE,
            supportingText = accountNumberError?.let { stringResource(id = it) }
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = officeName,
            onValueChange = { officeName = it },
            label = R.string.office_name,
            error = officeNameError != null && beneficiaryState != BeneficiaryState.UPDATE,
            enabled = beneficiaryState != BeneficiaryState.UPDATE,
            supportingText = officeNameError?.let { stringResource(id = it) }
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = transferLimit,
            onValueChange = { transferLimit = it },
            label = R.string.transfer_limit,
            keyboardType = KeyboardType.Number,
            error = transferLimitError != null,
            supportingText = transferLimitError?.let { stringResource(id = it) }
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = beneficiaryName,
            onValueChange = { beneficiaryName = it },
            label = R.string.beneficiary_name,
            error = beneficiaryNameError != null,
            supportingText = beneficiaryNameError?.let { stringResource(id = it) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        MifosTextButton(
            modifier = Modifier.fillMaxWidth(),
            textResId = R.string.submit_beneficiary,
            onClick = {
                validateFields()
                if ((beneficiaryState != BeneficiaryState.UPDATE && accountTypeError == null && accountNumberError == null && officeNameError == null) &&
                    transferLimitError == null && beneficiaryNameError == null) {
                    onSubmit(
                        BeneficiaryPayload(
                            name = beneficiaryName.text,
                            accountNumber = accountNumber.text,
                            transferLimit = transferLimit.text.toFloat(),
                            officeName = officeName.text,
                            accountType = accountType
                        )
                    )
                } else if (beneficiaryState == BeneficiaryState.UPDATE && transferLimitError == null && beneficiaryNameError == null) {
                    onSubmit(
                        BeneficiaryPayload(
                            name = beneficiaryName.text,
                            accountNumber = accountNumber.text,
                            transferLimit = transferLimit.text.toFloat(),
                            officeName = officeName.text,
                            accountType = accountType
                        )
                    )
                }
            },
        )
    }
}


@Composable
@Preview(showSystemUi = true)
fun BeneficiaryApplicationContentPreview() {
    MifosMobileTheme {
        BeneficiaryApplicationContent(
            prefilledBeneficiary = null,
            beneficiaryTemplate = BeneficiaryTemplate(),
            beneficiaryState = BeneficiaryState.CREATE_QR,
            onSubmit = {},
        )
    }
}