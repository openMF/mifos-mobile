package org.mifos.mobile.ui.savings_account_transaction


import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosCheckBox
import org.mifos.mobile.core.ui.component.MifosIconTextButton
import org.mifos.mobile.core.ui.component.MifosIcons
import org.mifos.mobile.core.ui.component.MifosRadioButton
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.DateHelper.getDateAsStringFromLong
import java.time.Instant

@Composable
fun SavingsTransactionFilterDialog(
    onDismiss: () -> Unit,
    savingsTransactionFilterDataModel: SavingsTransactionFilterDataModel,
    filter: (SavingsTransactionFilterDataModel) -> Unit,
) {

    var radioFilter by rememberSaveable { mutableStateOf(savingsTransactionFilterDataModel.radioFilter) }
    val checkBoxFilters by rememberSaveable { mutableStateOf(savingsTransactionFilterDataModel.checkBoxFilters) }
    var startDate by rememberSaveable { mutableStateOf(savingsTransactionFilterDataModel.startDate) }
    var endDate by rememberSaveable { mutableStateOf(savingsTransactionFilterDataModel.endDate) }

    var isDepositChecked by rememberSaveable { mutableStateOf(false) }
    var isDividendPayoutChecked by rememberSaveable { mutableStateOf(false) }
    var isWithdrawalChecked by rememberSaveable { mutableStateOf(false) }
    var isInterestPostingChecked by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = checkBoxFilters) {
        checkBoxFilters.forEach { filter ->
            when(filter) {
                SavingsTransactionCheckBoxFilter.DEPOSIT -> isDepositChecked = true
                SavingsTransactionCheckBoxFilter.DIVIDEND_PAYOUT -> isDividendPayoutChecked = true
                SavingsTransactionCheckBoxFilter.WITHDRAWAL -> isWithdrawalChecked = true
                SavingsTransactionCheckBoxFilter.INTEREST_POSTING -> isInterestPostingChecked = true
            }
        }
    }

    Dialog(
        onDismissRequest = { onDismiss.invoke() },
    ) {
        Card(shape = RoundedCornerShape(20.dp)) {
            Column(
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp)
            ) {
                Text(text = stringResource(id = R.string.select_you_want))

                Spacer(modifier = Modifier.height(20.dp))

                SavingsTransactionFilterDialogContent(
                    selectedStartDate = startDate,
                    selectedEndDate = endDate,
                    radioFilter = radioFilter,
                    selectRadioFilter = { radioFilter = it },
                    setStartDate = { startDate = it },
                    isDepositChecked = isDepositChecked,
                    isWithdrawalChecked = isWithdrawalChecked,
                    isInterestPostingChecked = isInterestPostingChecked,
                    isDividendPayoutChecked = isDividendPayoutChecked,
                    setEndDate = { endDate = it },
                    toggleCheckBox = { filter, isEnabled ->
                        when(filter) {
                            SavingsTransactionCheckBoxFilter.DEPOSIT -> isDepositChecked = isEnabled
                            SavingsTransactionCheckBoxFilter.DIVIDEND_PAYOUT -> isDividendPayoutChecked = isEnabled
                            SavingsTransactionCheckBoxFilter.WITHDRAWAL -> isWithdrawalChecked = isEnabled
                            SavingsTransactionCheckBoxFilter.INTEREST_POSTING -> isInterestPostingChecked = isEnabled
                        }
                        if(isEnabled) checkBoxFilters.add(filter)
                        else checkBoxFilters.remove(filter)
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row {
                    TextButton(
                        onClick = {
                            radioFilter = null
                            isDepositChecked = false
                            isWithdrawalChecked = false
                            isInterestPostingChecked = false
                            isDividendPayoutChecked = false
                            checkBoxFilters.clear()
                        }
                    ) {
                        Text(text = stringResource(id = R.string.clear_filters))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    TextButton(
                        onClick = { onDismiss() }
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }

                    TextButton(
                        onClick = {
                            onDismiss()
                            filter(
                                SavingsTransactionFilterDataModel(
                                    startDate = startDate,
                                    endDate = endDate,
                                    radioFilter = radioFilter,
                                    checkBoxFilters = checkBoxFilters
                                )
                            )
                        }
                    ) {
                        Text(text = stringResource(id = R.string.filter))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsTransactionFilterDialogContent(
    selectedStartDate: Long,
    selectedEndDate: Long,
    radioFilter: SavingsTransactionRadioFilter?,
    selectRadioFilter: (SavingsTransactionRadioFilter) -> Unit,
    isDepositChecked: Boolean,
    isDividendPayoutChecked: Boolean,
    isWithdrawalChecked: Boolean,
    isInterestPostingChecked: Boolean,
    toggleCheckBox: (SavingsTransactionCheckBoxFilter, Boolean) -> Unit,
    setStartDate: (Long) -> Unit,
    setEndDate: (Long) -> Unit
) {
    val scrollState = rememberScrollState()
    var showStartDatePickerDialog by rememberSaveable { mutableStateOf(false) }
    var showEndDatePickerDialog by rememberSaveable { mutableStateOf(false) }
    val startDatePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedStartDate)
    val endDatePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedEndDate)
    var isDatesEnabled by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = radioFilter) {
        isDatesEnabled = radioFilter == SavingsTransactionRadioFilter.DATE

        when (radioFilter) {
            SavingsTransactionRadioFilter.FOUR_WEEKS -> {
                setStartDate(DateHelper.subtractWeeks(4))
                setEndDate(System.currentTimeMillis())
            }

            SavingsTransactionRadioFilter.THREE_MONTHS -> {
                setStartDate(DateHelper.subtractMonths(3))
                setEndDate(System.currentTimeMillis())
            }

            SavingsTransactionRadioFilter.SIX_MONTHS -> {
                setStartDate(DateHelper.subtractMonths(6))
                setEndDate(System.currentTimeMillis())
            }

            else -> Unit
        }
    }

    Column(
        modifier = Modifier.scrollable(state = scrollState, orientation = Orientation.Vertical)
    ) {
        SavingsTransactionRadioFilter.entries.forEach { filter ->
            MifosRadioButton(
                selected = radioFilter == filter,
                onClick = { selectRadioFilter(filter) },
                textResId = filter.textResId
            )

            if (filter == SavingsTransactionRadioFilter.DATE) {
                Row {
                    MifosIconTextButton(
                        text = getDateAsStringFromLong(selectedStartDate),
                        imageVector = MifosIcons.Edit,
                        enabled = radioFilter == SavingsTransactionRadioFilter.DATE,
                        onClick = { showStartDatePickerDialog = true }
                    )
                    MifosIconTextButton(
                        text = getDateAsStringFromLong(selectedEndDate),
                        imageVector = MifosIcons.Edit,
                        enabled = radioFilter == SavingsTransactionRadioFilter.DATE,
                        onClick = { showEndDatePickerDialog = true }
                    )
                }
            }
        }

        SavingsTransactionCheckBoxFilter.entries.forEach { filter ->
            MifosCheckBox(
                checked = when(filter) {
                    SavingsTransactionCheckBoxFilter.DEPOSIT -> isDepositChecked
                    SavingsTransactionCheckBoxFilter.DIVIDEND_PAYOUT -> isDividendPayoutChecked
                    SavingsTransactionCheckBoxFilter.WITHDRAWAL -> isWithdrawalChecked
                    SavingsTransactionCheckBoxFilter.INTEREST_POSTING -> isInterestPostingChecked
                },
                onCheckChanged = {
                    toggleCheckBox(filter, it)
                },
                text = stringResource(id = filter.textResId),
                checkboxColors = CheckboxDefaults.colors().copy(
                    checkedBorderColor = filter.checkBoxColor,
                    uncheckedBorderColor = filter.checkBoxColor,
                    checkedBoxColor = filter.checkBoxColor,
                )
            )
        }
    }

    if (showStartDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showStartDatePickerDialog = false },
            confirmButton = {
                startDatePickerState.selectedDateMillis?.let{ setStartDate(it) }
            }
        ) { DatePicker(state = startDatePickerState) }
    }

    if (showEndDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePickerDialog = false },
            confirmButton = {
                endDatePickerState.selectedDateMillis?.let { setEndDate(it) }
            }
        ) { DatePicker(state = endDatePickerState) }
    }
}


@Preview
@Composable
fun SavingsTransactionFilterDialogPreview() {
    MifosMobileTheme {
        SavingsTransactionFilterDialog(
            savingsTransactionFilterDataModel = SavingsTransactionFilterDataModel(
                radioFilter = null,
                checkBoxFilters = mutableListOf(),
                startDate = Instant.now().toEpochMilli(),
                endDate = Instant.now().toEpochMilli()
            ),
            filter = {},
            onDismiss = {},
        )
    }
}


