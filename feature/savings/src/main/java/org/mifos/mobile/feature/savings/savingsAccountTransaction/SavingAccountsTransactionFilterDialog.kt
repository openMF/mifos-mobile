/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.savingsAccountTransaction

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.common.utils.DateHelper
import org.mifos.mobile.core.common.utils.DateHelper.getDateAsStringFromLong
import org.mifos.mobile.core.designsystem.components.MifosIconTextButton
import org.mifos.mobile.core.designsystem.components.MifosRadioButton
import org.mifos.mobile.core.designsystem.components.MifosTextButton
import org.mifos.mobile.core.designsystem.icons.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosCheckBox
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.savings.R
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SavingsTransactionFilterDialog(
    onDismiss: () -> Unit,
    savingsTransactionFilterDataModel: SavingsTransactionFilterDataModel,
    filter: (SavingsTransactionFilterDataModel) -> Unit,
    modifier: Modifier = Modifier,
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
            when (filter) {
                SavingsTransactionCheckBoxFilter.DEPOSIT -> isDepositChecked = true
                SavingsTransactionCheckBoxFilter.DIVIDEND_PAYOUT -> isDividendPayoutChecked = true
                SavingsTransactionCheckBoxFilter.WITHDRAWAL -> isWithdrawalChecked = true
                SavingsTransactionCheckBoxFilter.INTEREST_POSTING -> isInterestPostingChecked = true
            }
        }
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        Card(shape = RoundedCornerShape(20.dp)) {
            Column(
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp),
            ) {
                Text(text = stringResource(id = R.string.select_you_want))

                Spacer(modifier = Modifier.height(20.dp))

                SavingsTransactionFilterDialogContent(
                    selectedStartDate = startDate,
                    selectedEndDate = endDate,
                    radioFilter = radioFilter,
                    isDepositChecked = isDepositChecked,
                    isDividendPayoutChecked = isDividendPayoutChecked,
                    isWithdrawalChecked = isWithdrawalChecked,
                    isInterestPostingChecked = isInterestPostingChecked,
                    selectRadioFilter = { radioFilter = it },
                    toggleCheckBox = { filter, isEnabled ->
                        when (filter) {
                            SavingsTransactionCheckBoxFilter.DEPOSIT -> isDepositChecked = isEnabled
                            SavingsTransactionCheckBoxFilter.DIVIDEND_PAYOUT ->
                                isDividendPayoutChecked =
                                    isEnabled

                            SavingsTransactionCheckBoxFilter.WITHDRAWAL ->
                                isWithdrawalChecked =
                                    isEnabled

                            SavingsTransactionCheckBoxFilter.INTEREST_POSTING ->
                                isInterestPostingChecked =
                                    isEnabled
                        }
                        if (isEnabled) {
                            checkBoxFilters.add(filter)
                        } else {
                            checkBoxFilters.remove(filter)
                        }
                    },
                    setStartDate = { startDate = it },
                    setEndDate = { endDate = it },
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row {
                    MifosTextButton(
                        text = stringResource(R.string.clear_filters),
                        onClick = {
                            radioFilter = null
                            isDepositChecked = false
                            isWithdrawalChecked = false
                            isInterestPostingChecked = false
                            isDividendPayoutChecked = false
                            checkBoxFilters.clear()
                        },
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    MifosTextButton(
                        onClick = onDismiss,
                        text = stringResource(id = R.string.cancel),
                    )

                    MifosTextButton(
                        text = stringResource(id = R.string.filter),
                        onClick = {
                            onDismiss()
                            filter(
                                SavingsTransactionFilterDataModel(
                                    startDate = startDate,
                                    endDate = endDate,
                                    radioFilter = radioFilter,
                                    checkBoxFilters = checkBoxFilters,
                                ),
                            )
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SavingsTransactionFilterDialogContent(
    selectedStartDate: Long,
    selectedEndDate: Long,
    radioFilter: SavingsTransactionRadioFilter?,
    isDepositChecked: Boolean,
    isDividendPayoutChecked: Boolean,
    isWithdrawalChecked: Boolean,
    isInterestPostingChecked: Boolean,
    selectRadioFilter: (SavingsTransactionRadioFilter) -> Unit,
    toggleCheckBox: (SavingsTransactionCheckBoxFilter, Boolean) -> Unit,
    setStartDate: (Long) -> Unit,
    setEndDate: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    var showStartDatePickerDialog by rememberSaveable { mutableStateOf(false) }
    var showEndDatePickerDialog by rememberSaveable { mutableStateOf(false) }
    val startDatePickerState =
        rememberDatePickerState(initialSelectedDateMillis = selectedStartDate)
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
        modifier = modifier
            .scrollable(state = scrollState, orientation = Orientation.Vertical),
    ) {
        SavingsTransactionRadioFilter.entries.forEach { filter ->
            MifosRadioButton(
                selected = radioFilter == filter,
                onClick = { selectRadioFilter(filter) },
                textResId = filter.textResId,
            )

            if (filter == SavingsTransactionRadioFilter.DATE) {
                Row {
                    MifosIconTextButton(
                        text = getDateAsStringFromLong(selectedStartDate),
                        imageVector = MifosIcons.Edit,
                        enabled = radioFilter == SavingsTransactionRadioFilter.DATE,
                        onClick = { showStartDatePickerDialog = true },
                    )
                    MifosIconTextButton(
                        text = getDateAsStringFromLong(selectedEndDate),
                        imageVector = MifosIcons.Edit,
                        enabled = radioFilter == SavingsTransactionRadioFilter.DATE,
                        onClick = { showEndDatePickerDialog = true },
                    )
                }
            }
        }

        SavingsTransactionCheckBoxFilter.entries.forEach { filter ->
            MifosCheckBox(
                checked = when (filter) {
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
                ),
            )
        }
    }

    if (showStartDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showStartDatePickerDialog = false },
            confirmButton = {
                startDatePickerState.selectedDateMillis?.let { setStartDate(it) }
            },
        ) { DatePicker(state = startDatePickerState) }
    }

    if (showEndDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePickerDialog = false },
            confirmButton = {
                endDatePickerState.selectedDateMillis?.let { setEndDate(it) }
            },
        ) { DatePicker(state = endDatePickerState) }
    }
}

@DevicePreviews
@Composable
private fun SavingsTransactionFilterDialogPreview() {
    MifosMobileTheme {
        SavingsTransactionFilterDialog(
            savingsTransactionFilterDataModel = SavingsTransactionFilterDataModel(
                radioFilter = null,
                checkBoxFilters = mutableListOf(),
                startDate = Instant.now().toEpochMilli(),
                endDate = Instant.now().toEpochMilli(),
            ),
            filter = {},
            onDismiss = {},
        )
    }
}
