/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.savingsMakeTransfer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.savings.generated.resources.Res
import mifos_mobile.feature.savings.generated.resources.amount
import mifos_mobile.feature.savings.generated.resources.amount_greater_than_zero
import mifos_mobile.feature.savings.generated.resources.cancel
import mifos_mobile.feature.savings.generated.resources.continue_str
import mifos_mobile.feature.savings.generated.resources.enter_amount
import mifos_mobile.feature.savings.generated.resources.enter_remarks
import mifos_mobile.feature.savings.generated.resources.four
import mifos_mobile.feature.savings.generated.resources.invalid_amount
import mifos_mobile.feature.savings.generated.resources.one
import mifos_mobile.feature.savings.generated.resources.pay_from
import mifos_mobile.feature.savings.generated.resources.remark
import mifos_mobile.feature.savings.generated.resources.required
import mifos_mobile.feature.savings.generated.resources.review
import mifos_mobile.feature.savings.generated.resources.select_pay_from
import mifos_mobile.feature.savings.generated.resources.select_pay_to
import mifos_mobile.feature.savings.generated.resources.three
import mifos_mobile.feature.savings.generated.resources.two
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosOutlinedButton
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.core.ui.component.MFStepProcess
import org.mifos.mobile.core.ui.component.MifosDropDownDoubleTextField
import org.mifos.mobile.core.ui.component.StepProcessState
import org.mifos.mobile.core.ui.component.getStepState

@Composable
internal fun SavingsMakeTransferContent(
    uiData: SavingsMakeTransferUiData,
    reviewTransfer: (ReviewTransferPayload) -> Unit,
    modifier: Modifier = Modifier,
    onCancelledClicked: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    var payToAccount by rememberSaveable { mutableStateOf(uiData.toAccountOptionPrefilled) }
    var payFromAccount by rememberSaveable { mutableStateOf(uiData.fromAccountOptionPrefilled) }
    var amount by rememberSaveable { mutableStateOf("") }
    var remark by rememberSaveable { mutableStateOf("") }

    var currentStep by rememberSaveable {
        mutableIntStateOf(if (uiData.toAccountOptionPrefilled == null) 0 else 1)
    }

    val payToStepState by remember {
        derivedStateOf { getStepState(targetStep = 0, currentStep = currentStep) }
    }

    val payFromStepState by remember {
        derivedStateOf { getStepState(targetStep = 1, currentStep = currentStep) }
    }

    val amountStepState by remember {
        derivedStateOf { getStepState(targetStep = 2, currentStep = currentStep) }
    }

    val remarkStepState by remember {
        derivedStateOf { getStepState(targetStep = 3, currentStep = currentStep) }
    }

    val stepsState = listOf(
        Pair(payToStepState, Res.string.one),
        Pair(payFromStepState, Res.string.two),
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
                deactivateColor = MaterialTheme.colorScheme.surfaceVariant,
                isLastStep = step == stepsState.last(),
            ) { processModifier ->
                when (step.second) {
                    Res.string.one -> PayToStepContent(
                        modifier = processModifier,
                        processState = payToStepState,
                        toAccountOptions = uiData.accountOptionsTemplate.toAccountOptions,
                        prefilledAccount = payToAccount,
                        onContinueClick = {
                            payToAccount = it
                            currentStep += 1
                        },
                    )

                    Res.string.two -> PayFromStep(
                        modifier = processModifier,
                        processState = payFromStepState,
                        fromAccountOptions = uiData.accountOptionsTemplate.fromAccountOptions.filter {
                            it.accountNo != payToAccount?.accountNo
                        },
                        prefilledAccount = payFromAccount,
                        onContinueClick = {
                            payFromAccount = it
                            currentStep += 1
                        },
                    )

                    Res.string.three -> EnterAmountStep(
                        processState = amountStepState,
                        onContinueClick = {
                            amount = it
                            currentStep += 1
                        },
                        modifier = processModifier,
                        outstandingBalance = uiData.outstandingBalance,
                    )

                    Res.string.four -> RemarkStep(
                        modifier = processModifier,
                        processState = remarkStepState,
                        onContinueClicked = {
                            remark = it
                            reviewTransfer(
                                ReviewTransferPayload(payToAccount, payFromAccount, amount, remark),
                            )
                        },
                        onCancelledClicked = onCancelledClicked,
                    )
                }
            }
        }
    }
}

@Composable
private fun PayToStepContent(
    toAccountOptions: List<AccountOption>,
    prefilledAccount: AccountOption?,
    processState: StepProcessState,
    onContinueClick: (AccountOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    var payToAccount by rememberSaveable { mutableStateOf(prefilledAccount) }
    var payToStepError by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        MifosDropDownDoubleTextField(
            optionsList = toAccountOptions.map { Pair(it.accountNo ?: "", it.clientName ?: "") },
            selectedOption = payToAccount?.accountNo ?: "",
            isEnabled = processState == StepProcessState.ACTIVE,
            labelResId = Res.string.select_pay_to,
            error = payToStepError,
            supportingText = stringResource(Res.string.required),
            onClick = { index, _ ->
                payToAccount = toAccountOptions[index]
                payToStepError = false
            },
        )
        if (processState == StepProcessState.ACTIVE) {
            MifosButton(
                content = { Text(stringResource(Res.string.continue_str)) },
                onClick = {
                    if (payToAccount == null) {
                        payToStepError = true
                    } else {
                        onContinueClick(payToAccount ?: AccountOption())
                    }
                },
            )
        }
    }
}

@Composable
private fun PayFromStep(
    fromAccountOptions: List<AccountOption>,
    prefilledAccount: AccountOption?,
    processState: StepProcessState,
    onContinueClick: (AccountOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    var payFromAccount by rememberSaveable { mutableStateOf(prefilledAccount) }
    var payFromError by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.pay_from),
            fontWeight = FontWeight.Bold,
        )
        if (processState == StepProcessState.ACTIVE) {
            MifosDropDownDoubleTextField(
                optionsList = fromAccountOptions.map {
                    Pair(
                        it.accountNo ?: "",
                        it.clientName ?: "",
                    )
                },
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
                content = { Text(stringResource(Res.string.continue_str)) },
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
private fun EnterAmountStep(
    processState: StepProcessState,
    onContinueClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    outstandingBalance: Double? = null,
) {
    var amount by remember { mutableStateOf(outstandingBalance?.toString() ?: "") }
    var amountError by remember { mutableStateOf<StringResource?>(null) }
    var showAmountError by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = amount) {
        showAmountError = false
        amountError = when {
            amount.trim() == "" -> Res.string.enter_amount
            amount.contains(".") -> Res.string.invalid_amount
            amount.toDoubleOrNull() == 0.0 -> Res.string.amount_greater_than_zero
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
                value = amount,
                onValueChange = { amount = it },
                config = MifosTextFieldConfig(
                    errorText = amountError?.let { stringResource(it) },
                    enabled = outstandingBalance == null,
                    isError = showAmountError,
                ),
                label = stringResource(Res.string.enter_amount),
            )
            MifosButton(
                content = { Text(stringResource(Res.string.continue_str)) },
                onClick = {
                    if (amountError == null) {
                        onContinueClick(amount)
                    } else {
                        showAmountError = true
                    }
                },
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
            remark.text.trim().isBlank() -> Res.string.enter_remarks
            else -> null
        }
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = stringResource(Res.string.remark),
            fontWeight = FontWeight.Bold,
        )
        if (processState == StepProcessState.ACTIVE) {
            TextField(
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent),
                value = remark,
                isError = showRemarkError,
                supportingText = { remarkError?.let { stringResource(it) } },
                onValueChange = { remark = it },
                label = { Text(text = stringResource(Res.string.remark)) },
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                MifosButton(
                    content = { Text(stringResource(Res.string.review)) },
                    onClick = {
                        remarkError?.let { showRemarkError = true }
                            ?: onContinueClicked(remark.text)
                    },
                )

                MifosOutlinedButton(
                    content = { Text(stringResource(Res.string.cancel)) },
                    onClick = onCancelledClicked,
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
