/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryApplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.account_number
import mifos_mobile.feature.beneficiary.generated.resources.beneficiary_name
import mifos_mobile.feature.beneficiary.generated.resources.office_name
import mifos_mobile.feature.beneficiary.generated.resources.select_account_type
import mifos_mobile.feature.beneficiary.generated.resources.submit_beneficiary
import mifos_mobile.feature.beneficiary.generated.resources.transfer_limit
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.component.MifosDropDownTextField

@Composable
@Suppress("CyclomaticComplexMethod", "ComplexCondition")
internal fun BeneficiaryApplicationContent(
    state: BeneficiaryApplicationState,
    onAction: (BeneficiaryApplicationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var accountType by rememberSaveable {
        mutableIntStateOf(
            state.beneficiary?.accountType?.id ?: -1,
        )
    }

    var accountNumber by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(state.beneficiary?.accountNumber ?: ""),
        )
    }

    var officeName by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(state.beneficiary?.officeName ?: ""),
        )
    }

    var transferLimit by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(
                state.beneficiary?.transferLimit?.toInt()?.toString() ?: "",
            ),
        )
    }

    var beneficiaryName by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(state.beneficiary?.name ?: ""),
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        MifosDropDownTextField(
            optionsList = state.template?.accountTypeOptions?.mapNotNull { it.value }
                ?: listOf(),
            labelResId = Res.string.select_account_type,
            selectedOption = state.template?.accountTypeOptions
                ?.firstOrNull { it.id == accountType }?.value ?: "",
            onClick = { index, _ ->
                accountType = state.template?.accountTypeOptions?.filter { it.value != null }
                    ?.get(index)?.id ?: -1
                onAction(BeneficiaryApplicationAction.OnFieldChange(accountType = accountType))
            },
            error = state.accountTypeError != null,
            isEnabled = state.beneficiaryState != BeneficiaryState.UPDATE,
            supportingText = state.accountTypeError?.let { stringResource(it) } ?: "",
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = accountNumber.text,
            onValueChange = {
                accountNumber = TextFieldValue(it)
                onAction(BeneficiaryApplicationAction.OnFieldChange(accountNumber = it))
            },
            label = stringResource(Res.string.account_number),
            config = MifosTextFieldConfig(
                isError = state.accountNumberError != null && state.beneficiaryState !=
                    BeneficiaryState.UPDATE,
                enabled = state.beneficiaryState != BeneficiaryState.UPDATE,
                errorText = state.accountNumberError?.let { stringResource(it) } ?: "",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            ),
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = officeName.text,
            onValueChange = {
                officeName = TextFieldValue(it)
                onAction(BeneficiaryApplicationAction.OnFieldChange(officeName = it))
            },
            label = stringResource(Res.string.office_name),
            config = MifosTextFieldConfig(
                isError = state.officeNameError != null && state.beneficiaryState !=
                    BeneficiaryState.UPDATE,
                enabled = state.beneficiaryState != BeneficiaryState.UPDATE,
                errorText = state.officeNameError?.let { stringResource(it) } ?: "",
            ),
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = transferLimit.text,
            onValueChange = {
                transferLimit = TextFieldValue(it)
                onAction(BeneficiaryApplicationAction.OnFieldChange(transferLimit = it))
            },
            label = stringResource(Res.string.transfer_limit),
            config = MifosTextFieldConfig(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
                isError = state.transferLimitError != null,
                errorText = state.transferLimitError?.let { stringResource(it) } ?: "",
            ),
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = beneficiaryName.text,
            onValueChange = {
                beneficiaryName = TextFieldValue(it)
                onAction(BeneficiaryApplicationAction.OnFieldChange(beneficiaryName = it))
            },
            label = stringResource(Res.string.beneficiary_name),
            config = MifosTextFieldConfig(
                isError = state.beneficiaryNameError != null,
                errorText = state.beneficiaryNameError?.let { stringResource(it) } ?: "",
            ),
        )

        Spacer(modifier = Modifier.height(10.dp))

        MifosButton(
            modifier = Modifier.fillMaxWidth(),
            text = { Text(text = stringResource(Res.string.submit_beneficiary)) },
            onClick = {
                onAction(
                    BeneficiaryApplicationAction.SubmitBeneficiary(
                        BeneficiaryPayload(
                            name = beneficiaryName.text,
                            accountNumber = accountNumber.text,
                            transferLimit = transferLimit.text.toIntOrNull() ?: 0,
                            officeName = officeName.text,
                            accountType = accountType,
                            locale = "en",
                        ),
                    ),
                )
            },
        )
    }
}

@Preview
@Composable
private fun BeneficiaryApplicationContentPreview() {
    MifosMobileTheme {
        BeneficiaryApplicationContent(
            state = BeneficiaryApplicationState(
                dialogState = null,
                beneficiaryState = BeneficiaryState.CREATE_QR,
            ),
            onAction = {},
        )
    }
}
