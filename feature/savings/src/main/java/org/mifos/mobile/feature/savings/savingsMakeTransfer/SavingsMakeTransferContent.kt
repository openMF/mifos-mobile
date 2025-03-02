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

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.designsystem.components.MifosButton
import org.mifos.mobile.core.designsystem.components.MifosOutlinedButton
import org.mifos.mobile.core.designsystem.components.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.theme.DarkGray
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.Primary
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.core.ui.component.MFStepProcess
import org.mifos.mobile.core.ui.component.MifosDropDownDoubleTextField
import org.mifos.mobile.core.ui.component.StepProcessState
import org.mifos.mobile.core.ui.component.getStepState
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.savings.R

@Composable
internal fun SavingsMakeTransferContent(
    uiData: SavingsMakeTransferViewModel.SavingsMakeTransferUiData,
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
        Pair(payToStepState, R.string.one),
        Pair(payFromStepState, R.string.two),
        Pair(amountStepState, R.string.three),
        Pair(remarkStepState, R.string.four),
    )

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(horizontal = 12.dp)
            .fillMaxSize(),
    ) {
        for (step in stepsState) {
            MFStepProcess(
                stepNumber = stringResource(id = step.second),
                activateColor = Primary,
                processState = step.first,
                deactivateColor = DarkGray,
                isLastStep = step == stepsState.last(),
            ) { processModifier ->
                Log.d(
                    "TAG-SavingsMakeTransferViewModel",
                    "SavingsMakeTransferContent: ToAccountOptions ${uiData.toAccountOptions}",
                )
                Log.d(
                    "TAG-SavingsMakeTransferViewModel",
                    "SavingsMakeTransferContent: fromAccountOptions ${uiData.fromAccountOptions}",
                )
                when (step.second) {
                    R.string.one -> PayToStepContent(
                        modifier = processModifier,
                        processState = payToStepState,
                        toAccountOptions = uiData.toAccountOptions,
                        prefilledAccount = payToAccount,
                        onContinueClick = {
                            payToAccount = it
                            currentStep += 1
                        },
                    )

                    R.string.two -> PayFromStep(
                        modifier = processModifier,
                        processState = payFromStepState,
                        fromAccountOptions = uiData.fromAccountOptions,
                        prefilledAccount = payFromAccount,
                        onContinueClick = {
                            payFromAccount = it
                            currentStep += 1
                        },
                    )

                    R.string.three -> EnterAmountStep(
                        processState = amountStepState,
                        onContinueClick = {
                            amount = it
                            currentStep += 1
                        },
                        modifier = processModifier,
                        outstandingBalance = uiData.outstandingBalance,
                    )

                    R.string.four -> RemarkStep(
                        modifier = processModifier,
                        processState = remarkStepState,
                        onContinueClicked = {
                            Log.d(
                                "TAG-After-remark",
                                "SavingsMakeTransferContent: payTo: $payToAccount, payFrom: $payFromAccount",
                            )
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
            labelResId = R.string.select_pay_to,
            error = payToStepError,
            supportingText = stringResource(id = R.string.required),
            onClick = { index, _ ->
                payToAccount = toAccountOptions[index]
                payToStepError = false
            },
        )
        if (processState == StepProcessState.ACTIVE) {
            MifosButton(
                textResId = R.string.continue_str,
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
            text = stringResource(id = R.string.pay_from),
            color = MaterialTheme.colorScheme.onSurface,
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
                labelResId = R.string.select_pay_from,
                error = payFromError,
                supportingText = stringResource(id = R.string.required),
                onClick = { index, _ ->
                    payFromAccount = fromAccountOptions[index]
                    payFromError = false
                },
            )
            MifosButton(
                textResId = R.string.continue_str,
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
    var amount by remember { mutableStateOf(TextFieldValue(outstandingBalance?.toString() ?: "")) }
    var amountError by rememberSaveable { mutableStateOf<Int?>(null) }
    var showAmountError by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = amount) {
        showAmountError = false
        amountError = when {
            amount.text.trim() == "" -> R.string.enter_amount
            amount.text.toDoubleOrNull() == 0.0 -> R.string.amount_greater_than_zero
            else -> null
        }
    }

    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.amount),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
        )
        if (processState == StepProcessState.ACTIVE) {
            MifosOutlinedTextField(
                modifier = Modifier,
                value = amount,
                keyboardType = KeyboardType.Number,
                onValueChange = { amount = it },
                error = showAmountError,
                supportingText = amountError?.let { stringResource(id = it) },
                enabled = outstandingBalance == null,
                label = R.string.enter_amount,
            )
            MifosButton(
                textResId = R.string.continue_str,
                onClick = {
                    if (amountError == null) {
                        onContinueClick(amount.text)
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
    var remarkError by rememberSaveable { mutableStateOf<Int?>(null) }
    var showRemarkError by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = remark) {
        showRemarkError = false
        remarkError = when {
            remark.text.trim().isBlank() -> R.string.enter_remarks
            else -> null
        }
    }

    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.remark),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
        )
        if (processState == StepProcessState.ACTIVE) {
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent),
                value = remark,
                isError = showRemarkError,
                supportingText = { remarkError?.let { stringResource(id = it) } },
                onValueChange = { remark = it },
                label = { Text(text = stringResource(id = R.string.remark)) },
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                MifosButton(
                    textResId = R.string.review,
                    onClick = {
                        remarkError?.let { showRemarkError = true }
                            ?: onContinueClicked(remark.text)
                    },
                )
                Spacer(modifier = Modifier.width(12.dp))
                MifosOutlinedButton(
                    textResId = R.string.cancel,
                    onClick = onCancelledClicked,
                )
            }
        } else {
            Text(
                text = stringResource(id = R.string.enter_remarks),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@DevicePreviews
@Composable
private fun SavingsMakeTransferContentPreview() {
    MifosMobileTheme {
        SavingsMakeTransferContent(
            uiData = SavingsMakeTransferViewModel.SavingsMakeTransferUiData(),
            reviewTransfer = {},
        )
    }
}
