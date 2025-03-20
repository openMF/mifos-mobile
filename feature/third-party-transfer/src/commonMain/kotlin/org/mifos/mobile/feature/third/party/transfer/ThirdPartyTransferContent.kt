/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.third.party.transfer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.third_party_transfer.generated.resources.Res
import mifos_mobile.feature.third_party_transfer.generated.resources.add_beneficiary
import mifos_mobile.feature.third_party_transfer.generated.resources.amount
import mifos_mobile.feature.third_party_transfer.generated.resources.amount_greater_than_zero
import mifos_mobile.feature.third_party_transfer.generated.resources.beneficiary
import mifos_mobile.feature.third_party_transfer.generated.resources.cancel
import mifos_mobile.feature.third_party_transfer.generated.resources.continue_str
import mifos_mobile.feature.third_party_transfer.generated.resources.enter_amount
import mifos_mobile.feature.third_party_transfer.generated.resources.enter_remarks
import mifos_mobile.feature.third_party_transfer.generated.resources.four
import mifos_mobile.feature.third_party_transfer.generated.resources.invalid_amount
import mifos_mobile.feature.third_party_transfer.generated.resources.loan_type
import mifos_mobile.feature.third_party_transfer.generated.resources.no_beneficiary_found_please_add
import mifos_mobile.feature.third_party_transfer.generated.resources.one
import mifos_mobile.feature.third_party_transfer.generated.resources.pay_from
import mifos_mobile.feature.third_party_transfer.generated.resources.remark
import mifos_mobile.feature.third_party_transfer.generated.resources.remark_is_mandatory
import mifos_mobile.feature.third_party_transfer.generated.resources.required
import mifos_mobile.feature.third_party_transfer.generated.resources.review
import mifos_mobile.feature.third_party_transfer.generated.resources.select_beneficiary
import mifos_mobile.feature.third_party_transfer.generated.resources.select_pay_from
import mifos_mobile.feature.third_party_transfer.generated.resources.select_pay_to
import mifos_mobile.feature.third_party_transfer.generated.resources.three
import mifos_mobile.feature.third_party_transfer.generated.resources.two
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosOutlinedButton
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosTextField
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.component.MFStepProcess
import org.mifos.mobile.core.ui.component.MifosDropDownDoubleTextField
import org.mifos.mobile.core.ui.component.StepProcessState
import org.mifos.mobile.core.ui.component.getStepState

@Composable
internal fun ThirdPartyTransferContent(
    state: ThirdPartyTransferState,
    onAction: (ThirdPartyTransferAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    var payFromAccount by rememberSaveable { mutableStateOf<AccountOption?>(null) }
    var beneficiary by rememberSaveable { mutableStateOf<Beneficiary?>(null) }
    var amount by rememberSaveable { mutableStateOf("") }
    var remark by rememberSaveable { mutableStateOf("") }

    var currentStep by rememberSaveable { mutableIntStateOf(0) }

    val payFromStepState by remember {
        derivedStateOf { getStepState(targetStep = 0, currentStep = currentStep) }
    }

    val beneficiaryStepState by remember {
        derivedStateOf { getStepState(targetStep = 1, currentStep = currentStep) }
    }

    val amountStepState by remember {
        derivedStateOf { getStepState(targetStep = 2, currentStep = currentStep) }
    }

    val remarkStepState by remember {
        derivedStateOf { getStepState(targetStep = 3, currentStep = currentStep) }
    }

    val stepsState = listOf(
        Pair(payFromStepState, Res.string.one),
        Pair(beneficiaryStepState, Res.string.two),
        Pair(amountStepState, Res.string.three),
        Pair(remarkStepState, Res.string.four),
    )

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(horizontal = 12.dp)
            .fillMaxSize(),
    ) {
        for (step in stepsState) {
            MFStepProcess(
                stepNumber = stringResource(step.second),
                activateColor = MaterialTheme.colorScheme.primary,
                processState = step.first,
                deactivateColor = DarkGray,
                isLastStep = step == stepsState.last(),
            ) { stepModifier ->
                when (step.second) {
                    Res.string.one -> state.fromAccountDetail?.let {
                        PayFromStep(
                            fromAccountOptions = it,
                            processState = payFromStepState,
                            onContinueClick = {
                                payFromAccount = it
                                currentStep += 1
                            },
                            modifier = stepModifier,
                        )
                    }

                    Res.string.two -> state.beneficiaries?.let {
                        BeneficiaryStep(
                            beneficiaryList = it,
                            processState = beneficiaryStepState,
                            addBeneficiary = { onAction(ThirdPartyTransferAction.OnAddBeneficiary) },
                            onContinueClick = {
                                beneficiary = it
                                currentStep += 1
                            },
                            modifier = stepModifier,
                        )
                    }

                    Res.string.three -> EnterAmountStep(
                        processState = amountStepState,
                        onContinueClick = {
                            amount = it
                            currentStep += 1
                        },
                        modifier = stepModifier,
                    )

                    Res.string.four -> RemarkStep(
                        processState = remarkStepState,
                        onContinueClicked = {
                            remark = it
                            onAction(
                                ThirdPartyTransferAction.OnReviewTransfer(
                                    ReviewTransferPayload(
                                        payFromAccount = payFromAccount!!,
                                        payToAccount = state.toAccountOption
                                            ?.firstOrNull { account ->
                                                account.accountNo == beneficiary?.accountNumber
                                            } ?: AccountOption(),
                                        amount = amount,
                                        review = remark,
                                    ),
                                    TransferType.TPT,
                                ),
                            )
                        },
                        modifier = stepModifier,
                        onCancelledClicked = { onAction(ThirdPartyTransferAction.OnNavigate) },
                    )
                }
            }
        }
    }
}

@Composable
private fun PayFromStep(
    fromAccountOptions: List<AccountOption>,
    processState: StepProcessState,
    onContinueClick: (AccountOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    var payFromAccount by rememberSaveable { mutableStateOf<AccountOption?>(null) }
    var payFromError by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.pay_from),
            fontWeight = FontWeight.Bold,
        )
        if (processState == StepProcessState.ACTIVE) {
            MifosDropDownDoubleTextField(
                optionsList = fromAccountOptions
                    .filter { it.accountType?.value != stringResource(Res.string.loan_type) }
                    .map { Pair(it.accountNo ?: "", it.accountType?.value ?: "") },
                selectedOption = payFromAccount?.accountNo ?: "",
                labelResId = Res.string.select_pay_from,
                error = payFromError,
                supportingText = stringResource(Res.string.required),
                onClick = { index, _ ->
                    payFromAccount = fromAccountOptions[index]
                    payFromError = false
                },
            )
            MifosButton(
                text = { Text(text = stringResource(Res.string.continue_str)) },
                onClick = {
                    if (payFromAccount == null) {
                        payFromError = true
                    } else {
                        onContinueClick(payFromAccount ?: AccountOption())
                    }
                },
            )
        }
    }
}

@Composable
private fun BeneficiaryStep(
    beneficiaryList: List<Beneficiary>,
    processState: StepProcessState,
    addBeneficiary: () -> Unit,
    onContinueClick: (Beneficiary) -> Unit,
    modifier: Modifier = Modifier,
) {
    var beneficiary by rememberSaveable { mutableStateOf<Beneficiary?>(null) }
    var beneficiaryError by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.beneficiary),
            fontWeight = FontWeight.Bold,
        )
        if (processState == StepProcessState.ACTIVE) {
            if (beneficiaryList.isEmpty()) {
                Text(
                    text = stringResource(Res.string.no_beneficiary_found_please_add),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelMedium,
                )
                MifosButton(
                    onClick = { addBeneficiary() },
                    text = { Text(text = stringResource(Res.string.add_beneficiary)) },
                )
            } else {
                MifosDropDownDoubleTextField(
                    optionsList = beneficiaryList
                        .map { Pair(it.accountNumber ?: "", it.name ?: "") },
                    selectedOption = beneficiary?.accountNumber ?: "",
                    labelResId = Res.string.select_pay_to,
                    error = beneficiaryError,
                    supportingText = stringResource(Res.string.required),
                    onClick = { index, _ ->
                        beneficiary = beneficiaryList[index]
                        beneficiaryError = false
                    },
                )
                MifosButton(
                    onClick = {
                        if (beneficiary == null) {
                            beneficiaryError = true
                        } else {
                            onContinueClick(beneficiary!!)
                        }
                    },
                    text = { Text(text = stringResource(Res.string.continue_str)) },
                )
            }
        } else {
            Text(
                text = stringResource(Res.string.select_beneficiary),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
private fun EnterAmountStep(
    processState: StepProcessState,
    onContinueClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var amount by remember { mutableStateOf(TextFieldValue("")) }
    var amountError by remember { mutableStateOf<StringResource?>(null) }
    var showAmountError by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = amount) {
        showAmountError = false
        amountError = when {
            amount.text.trim().isEmpty() -> Res.string.enter_amount
            amount.text.toDoubleOrNull() == null -> Res.string.invalid_amount
            amount.text.toDoubleOrNull() == 0.0 -> Res.string.amount_greater_than_zero
            else -> null
        }
    }

    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.amount),
            fontWeight = FontWeight.Bold,
        )
        if (processState == StepProcessState.ACTIVE) {
            MifosOutlinedTextField(
                modifier = Modifier,
                value = amount.text,
                onValueChange = { amount = TextFieldValue(it) },
                label = stringResource(Res.string.enter_amount),
                config = MifosTextFieldConfig(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    isError = showAmountError,
                    errorText = amountError?.let { stringResource(it) },
                ),
            )
            MifosButton(
                onClick = {
                    if (amountError == null) {
                        onContinueClick(amount.text)
                    } else {
                        showAmountError = true
                    }
                },
                text = { Text(text = stringResource(Res.string.continue_str)) },
            )
        } else {
            Text(
                text = stringResource(Res.string.enter_amount),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
private fun RemarkStep(
    processState: StepProcessState,
    onContinueClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    onCancelledClicked: () -> Unit = {},
) {
    var remark by remember { mutableStateOf(TextFieldValue("")) }
    var remarkError by remember { mutableStateOf<StringResource?>(null) }
    var showRemarkError by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = remark) {
        showRemarkError = false
        remarkError = when {
            remark.text.trim().isBlank() -> Res.string.remark_is_mandatory
            else -> null
        }
    }

    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.remark),
            fontWeight = FontWeight.Bold,
        )
        if (processState == StepProcessState.ACTIVE) {
            Spacer(modifier = Modifier.height(12.dp))
            MifosTextField(
                value = remark.text,
                config = MifosTextFieldConfig(
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                    ),
                    isError = showRemarkError,
                    errorText = remarkError?.let { stringResource(it) },
                ),
                onValueChange = { remark = TextFieldValue(it) },
                label = stringResource(Res.string.remark),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                MifosButton(
                    onClick = {
                        remarkError?.let { showRemarkError = true }
                            ?: onContinueClicked(remark.text)
                    },
                    text = { Text(text = stringResource(Res.string.review)) },
                )
                Spacer(modifier = Modifier.width(12.dp))
                MifosOutlinedButton(
                    onClick = { onCancelledClicked() },
                    content = { Text(text = stringResource(Res.string.cancel)) },

                )
            }
        } else {
            Text(
                text = stringResource(Res.string.enter_remarks),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Preview
@Composable
private fun ThirdPartyTransferContentPreview() {
    MifosMobileTheme {
        ThirdPartyTransferContent(
            state = ThirdPartyTransferState(dialogState = null),
            onAction = { },
            modifier = Modifier,
        )
    }
}
