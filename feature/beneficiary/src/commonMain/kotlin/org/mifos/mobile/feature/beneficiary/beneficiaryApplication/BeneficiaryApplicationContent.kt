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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.account_number
import mifos_mobile.feature.beneficiary.generated.resources.beneficiary_name
import mifos_mobile.feature.beneficiary.generated.resources.office_name
import mifos_mobile.feature.beneficiary.generated.resources.select_account_type
import mifos_mobile.feature.beneficiary.generated.resources.skip_the_form
import mifos_mobile.feature.beneficiary.generated.resources.submit_beneficiary
import mifos_mobile.feature.beneficiary.generated.resources.transfer_limit
import mifos_mobile.feature.beneficiary.generated.resources.upload_or_scan_qr_code
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.component.MifosDropDownTextField

/**
 * Composable function to display a beneficiary application form.
 *
 * @param state The current state of the beneficiary application form.
 * @param onAction A callback to handle actions from the form.
 * @param modifier The modifier to apply to the composable.
 */
@Composable
@Suppress("CyclomaticComplexMethod", "ComplexCondition")
internal fun BeneficiaryApplicationContent(
    state: BeneficiaryApplicationState,
    onAction: (BeneficiaryApplicationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = DesignToken.padding.large,
                vertical = DesignToken.padding.extraLargeIncreased,
            ),
    ) {
        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.beneficiaryName,
            onValueChange = {
                onAction(BeneficiaryApplicationAction.OnBeneficiaryNameChanged(beneficiaryName = it))
            },
            label = stringResource(Res.string.beneficiary_name),
            config = MifosTextFieldConfig(
                isError = state.beneficiaryNameError != null,
                errorText = state.beneficiaryNameError?.let { stringResource(it) } ?: "",
            ),
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.accountNumber,
            onValueChange = {
                onAction(BeneficiaryApplicationAction.OnAccountNumberChanged(accountNumber = it))
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

        MifosDropDownTextField(
            optionsList = state.template?.accountTypeOptions?.mapNotNull { it.value }
                ?: listOf(),
            labelResId = Res.string.select_account_type,
            selectedOption = state.template?.accountTypeOptions
                ?.firstOrNull { it.id == state.accountType }?.value ?: "",
            onClick = { index, _ ->
                val accountType = state.template?.accountTypeOptions?.filter { it.value != null }
                    ?.get(index)?.id ?: -1
                onAction(BeneficiaryApplicationAction.OnAccountTypeChanged(accountType = accountType))
            },
            error = state.accountTypeError != null,
            isEnabled = state.beneficiaryState != BeneficiaryState.UPDATE,
            supportingText = state.accountTypeError?.let { stringResource(it) } ?: "",
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.officeName,
            onValueChange = {
                onAction(BeneficiaryApplicationAction.OnOfficeNameChanged(officeName = it))
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
            value = state.transferLimit,
            onValueChange = {
                onAction(BeneficiaryApplicationAction.OnTransferLimitChanged(transferLimit = it))
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

        Spacer(Modifier.height(DesignToken.padding.large))

        MifosButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(DesignToken.sizes.buttonHeight),
            text = { Text(text = stringResource(Res.string.submit_beneficiary)) },
            onClick = {
                onAction(
                    BeneficiaryApplicationAction.SubmitBeneficiary,
                )
            },
            enabled = state.isEnabled,
        )

        Spacer(Modifier.height(DesignToken.padding.extraLargeIncreased))

        if (state.beneficiaryState == BeneficiaryState.CREATE_MANUAL) {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(Res.string.skip_the_form))
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append(stringResource(Res.string.upload_or_scan_qr_code))
                    }
                },
                modifier = Modifier.fillMaxWidth().clickable {
                    onAction(BeneficiaryApplicationAction.NavigateToQR)
                },
                style = MifosTypography.labelMediumEmphasized,
                textAlign = TextAlign.Center,
            )
        }
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
