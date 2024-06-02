package org.mifos.mobile.ui.savings_make_transfer

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MFStepProcess
import org.mifos.mobile.core.ui.component.MifosDropDownDoubleTextField
import org.mifos.mobile.core.ui.component.MifosOutlinedTextField
import org.mifos.mobile.core.ui.component.StepProcessState
import org.mifos.mobile.core.ui.component.getStepState
import org.mifos.mobile.core.ui.theme.DarkGray
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.theme.Primary
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.payload.AccountDetail
import org.mifos.mobile.models.templates.account.AccountOption
import org.mifos.mobile.ui.loan_account_withdraw.LoanAccountWithdrawScreen
import org.mifos.mobile.ui.loan_account_withdraw.LoanAccountWithdrawUiState

@Composable
fun SavingsMakeTransferContent(
    uiData: SavingsMakeTransferUiData,
    onCancelledClicked: () -> Unit = {},
    reviewTransfer: (ReviewTransferPayload) -> Unit
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
        Pair(remarkStepState, R.string.four)
    )

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(horizontal = 12.dp)
            .fillMaxSize()
    ) {
        for (step in stepsState) {
            MFStepProcess(
                stepNumber = stringResource(id = step.second),
                activateColor = Primary,
                processState = step.first,
                deactivateColor = DarkGray,
                isLastStep = step == stepsState.last()
            ) { modifier ->
                when (step.second) {
                    R.string.one -> PayToStepContent(modifier = modifier,
                        processState = payToStepState,
                        toAccountOptions = uiData.accountOptionsTemplate.fromAccountOptions,
                        prefilledAccount = payToAccount,
                        onContinueClick = {
                            payToAccount = it
                            currentStep += 1
                        }
                    )

                    R.string.two -> PayFromStep(modifier = modifier,
                        processState = payFromStepState,
                        fromAccountOptions = uiData.accountOptionsTemplate.fromAccountOptions,
                        prefilledAccount = payFromAccount,
                        onContinueClick = {
                            payFromAccount = it
                            currentStep += 1
                        }
                    )

                    R.string.three -> EnterAmountStep(modifier = modifier,
                        processState = amountStepState,
                        outstandingBalance = uiData.outstandingBalance,
                        onContinueClick = {
                            amount = it
                            currentStep += 1
                        }
                    )

                    R.string.four -> RemarkStep(
                        modifier = modifier,
                        processState = remarkStepState,
                        onContinueClicked = {
                            remark = it
                            reviewTransfer(
                                ReviewTransferPayload(payToAccount, payFromAccount, amount, remark)
                            )
                        },
                        onCancelledClicked = onCancelledClicked
                    )
                }
            }
        }
    }
}

@Composable
fun PayToStepContent(
    modifier: Modifier,
    toAccountOptions: List<AccountOption>,
    prefilledAccount: AccountOption?,
    processState: StepProcessState,
    onContinueClick: (AccountOption) -> Unit,
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
            }
        )
        if (processState == StepProcessState.ACTIVE) {
            Button(onClick = {
                if (payToAccount == null) payToStepError = true
                else onContinueClick(payToAccount ?: AccountOption())
            }, content = {
                Text(text = stringResource(id = R.string.continue_str))
            })
        }
    }
}

@Composable
fun PayFromStep(
    modifier: Modifier,
    fromAccountOptions: List<AccountOption>,
    prefilledAccount: AccountOption?,
    processState: StepProcessState,
    onContinueClick: (AccountOption) -> Unit
) {

    var payFromAccount by rememberSaveable { mutableStateOf(prefilledAccount) }
    var payFromError by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.pay_from),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        if (processState == StepProcessState.ACTIVE) {
            MifosDropDownDoubleTextField(
                optionsList = fromAccountOptions.map {
                    Pair(
                        it.accountNo ?: "",
                        it.clientName ?: ""
                    )
                },
                selectedOption = payFromAccount?.accountNo ?: "",
                labelResId = R.string.select_pay_from,
                error = payFromError,
                supportingText = stringResource(id = R.string.required),
                onClick = { index, _ ->
                    payFromAccount = fromAccountOptions[index]
                    payFromError = false
                }
            )
            Button(onClick = {
                if (payFromAccount == null) payFromError = true
                else onContinueClick(payFromAccount ?: AccountOption())
            }, content = {
                Text(text = stringResource(id = R.string.continue_str))
            })
        }
    }
}

@Composable
fun EnterAmountStep(
    modifier: Modifier,
    outstandingBalance: Double? = null,
    processState: StepProcessState,
    onContinueClick: (String) -> Unit
) {
    val context = LocalContext.current
    var amount by remember { mutableStateOf(TextFieldValue(outstandingBalance?.toString() ?: "")) }
    var amountError by rememberSaveable { mutableStateOf<Int?>(null) }
    var showAmountError by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = amount) {
        showAmountError = false
        amountError = when {
            amount.text.trim() == "" -> R.string.enter_amount
            amount.text.contains(".") -> R.string.invalid_amount
            amount.text.toDoubleOrNull() == 0.0 -> R.string.amount_greater_than_zero
            else -> null
        }
    }

    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.amount),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
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
            Button(
                onClick = {
                    if(amountError == null) { onContinueClick(amount.text) }
                    else { showAmountError = true }
                },
                content = {
                    Text(text = stringResource(id = R.string.continue_str))
                }
            )
        }
    }
}

@Composable
fun RemarkStep(
    modifier: Modifier,
    processState: StepProcessState,
    onContinueClicked: (String) -> Unit,
    onCancelledClicked: () -> Unit = {}
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
            fontWeight = FontWeight.Bold
        )
        if (processState == StepProcessState.ACTIVE) {
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent),
                value = remark,
                isError = showRemarkError,
                supportingText = { remarkError?.let { stringResource(id = it) } },
                onValueChange = { remark = it },
                label = { Text(text = stringResource(id = R.string.remark)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Button(
                    onClick = {
                        remarkError?.let { showRemarkError = true }
                            ?: onContinueClicked(remark.text)
                    },
                    content = { Text(text = stringResource(id = R.string.review)) }
                )
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedButton(
                    onClick = { onCancelledClicked() },
                    content = { Text(text = stringResource(id = R.string.cancel)) }
                )
            }
        } else {
            Text(
                text = stringResource(id = R.string.enter_remarks),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SavingsMakeTransferContentPreview() {
    MifosMobileTheme {
        SavingsMakeTransferContent(
            uiData = SavingsMakeTransferUiData(),
            reviewTransfer = {}
        )
    }
}

