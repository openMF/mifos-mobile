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

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import org.mifos.mobile.core.common.Constants.SAVINGS_ID
import org.mifos.mobile.core.common.utils.DateHelper
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.designsystem.theme.DepositGreen
import org.mifos.mobile.core.designsystem.theme.GreenSuccess
import org.mifos.mobile.core.designsystem.theme.RedLight
import org.mifos.mobile.core.model.entity.accounts.savings.TransactionType
import org.mifos.mobile.core.model.entity.accounts.savings.Transactions
import org.mifos.mobile.feature.savings.R
import org.mifos.mobile.feature.savings.savingsAccountTransaction.SavingsAccountTransactionUiState.Loading
import javax.inject.Inject

@HiltViewModel
internal class SavingAccountsTransactionViewModel @Inject constructor(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val mUiState = MutableStateFlow<SavingsAccountTransactionUiState>(Loading)
    val uiState = mUiState.asStateFlow()

    private var _transactionsList: List<Transactions> = mutableListOf()
    private val transactionsList: List<Transactions> get() = _transactionsList

    private val savingsId: StateFlow<Long> = savedStateHandle.getStateFlow(
        key = SAVINGS_ID,
        initialValue = -1L,
    )

    init {
        loadSavingsWithAssociations()
    }

    fun loadSavingsWithAssociations(accountId: Long = savingsId.value) {
        viewModelScope.launch {
            mUiState.value = Loading
            savingsAccountRepositoryImp.getSavingsWithAssociations(
                accountId,
                org.mifos.mobile.core.common.Constants.TRANSACTIONS,
            ).catch {
                mUiState.value =
                    SavingsAccountTransactionUiState.Error(it.message)
            }.collect {
                _transactionsList = it.transactions
                mUiState.value =
                    SavingsAccountTransactionUiState.Success(it.transactions)
            }
        }
    }

    fun filterList(filter: SavingsTransactionFilterDataModel) {
        when {
            filter.radioFilter != null && filter.checkBoxFilters.isNotEmpty() -> {
                filterByDateAndType(
                    startDate = filter.startDate,
                    endDate = filter.endDate,
                    checkBoxFilters = filter.checkBoxFilters,
                )
            }

            filter.radioFilter != null -> {
                filterByDate(
                    startDate = filter.startDate,
                    endDate = filter.endDate,
                )
            }

            filter.checkBoxFilters.isNotEmpty() -> {
                filterByType(
                    checkBoxFilters = filter.checkBoxFilters,
                )
            }

            else -> {
                mUiState.value =
                    SavingsAccountTransactionUiState.Success(transactionsList)
            }
        }
    }

    private fun filterByDateAndType(
        startDate: Long,
        endDate: Long,
        checkBoxFilters: MutableList<SavingsTransactionCheckBoxFilter>,
    ) {
        val typeFilteredList =
            filterSavingsAccountTransactionsByType(checkBoxFilters = checkBoxFilters)
        val dateAndTypeFilteredList = filterTransactionListByDate(
            transactions = typeFilteredList,
            startDate = startDate,
            endDate = endDate,
        )
        mUiState.value =
            SavingsAccountTransactionUiState.Success(dateAndTypeFilteredList)
    }

    private fun filterByDate(startDate: Long, endDate: Long) {
        val list = filterTransactionListByDate(
            transactions = transactionsList,
            startDate = startDate,
            endDate = endDate,
        )
        mUiState.value = SavingsAccountTransactionUiState.Success(list)
    }

    private fun filterByType(
        checkBoxFilters: MutableList<SavingsTransactionCheckBoxFilter>,
    ) {
        val list = filterSavingsAccountTransactionsByType(checkBoxFilters = checkBoxFilters)
        mUiState.value = SavingsAccountTransactionUiState.Success(list)
    }

    private fun filterTransactionListByDate(
        transactions: List<Transactions>,
        startDate: Long,
        endDate: Long,
    ): List<Transactions> {
        return transactions.filter {
            (DateHelper.getDateAsLongFromList(it.date) in startDate..endDate)
        }
    }

    private fun filterSavingsAccountTransactionsByType(
        checkBoxFilters: MutableList<SavingsTransactionCheckBoxFilter>,
    ): List<Transactions> {
        var filteredSavingsTransactions: List<Transactions> = ArrayList()
        checkBoxFilters.forEach { filter ->
            val list = when (filter) {
                SavingsTransactionCheckBoxFilter.DEPOSIT -> {
                    transactionsList.filter { it.transactionType?.deposit == true }
                }

                SavingsTransactionCheckBoxFilter.DIVIDEND_PAYOUT -> {
                    transactionsList.filter { it.transactionType?.dividendPayout == true }
                }

                SavingsTransactionCheckBoxFilter.WITHDRAWAL -> {
                    transactionsList.filter { it.transactionType?.withdrawal == true }
                }

                SavingsTransactionCheckBoxFilter.INTEREST_POSTING -> {
                    transactionsList.filter { it.transactionType?.interestPosting == true }
                }
            }
            filteredSavingsTransactions = filteredSavingsTransactions.plus(list)
        }
        return filteredSavingsTransactions
    }
}

internal sealed class SavingsAccountTransactionUiState {
    data object Loading : SavingsAccountTransactionUiState()
    data class Error(val errorMessage: String?) : SavingsAccountTransactionUiState()
    data class Success(val savingAccountsTransactionList: List<Transactions>?) :
        SavingsAccountTransactionUiState()
}

internal fun getTransactionTriangleResId(transactionType: TransactionType?): Int {
    return transactionType?.run {
        when {
            deposit == true -> R.drawable.triangular_green_view
            dividendPayout == true -> R.drawable.triangular_red_view
            withdrawal == true -> R.drawable.triangular_red_view
            interestPosting == true -> R.drawable.triangular_green_view
            feeDeduction == true -> R.drawable.triangular_red_view
            initiateTransfer == true -> R.drawable.triangular_red_view
            approveTransfer == true -> R.drawable.triangular_red_view
            withdrawTransfer == true -> R.drawable.triangular_red_view
            rejectTransfer == true -> R.drawable.triangular_green_view
            overdraftFee == true -> R.drawable.triangular_red_view
            else -> R.drawable.triangular_green_view
        }
    } ?: R.drawable.triangular_red_view
}

@Parcelize
internal data class SavingsTransactionFilterDataModel(
    val startDate: Long,
    val endDate: Long,
    val radioFilter: SavingsTransactionRadioFilter?,
    val checkBoxFilters: MutableList<SavingsTransactionCheckBoxFilter>,
) : Parcelable

internal enum class SavingsTransactionRadioFilter(val textResId: Int) {
    DATE(textResId = R.string.date),
    FOUR_WEEKS(textResId = R.string.four_weeks),
    THREE_MONTHS(textResId = R.string.three_months),
    SIX_MONTHS(textResId = R.string.six_months),
}

internal enum class SavingsTransactionCheckBoxFilter(
    val textResId: Int,
    val checkBoxColor: Color,
) {
    DEPOSIT(
        textResId = R.string.deposit,
        checkBoxColor = DepositGreen,
    ),
    DIVIDEND_PAYOUT(
        textResId = R.string.dividend_payout,
        checkBoxColor = RedLight,
    ),
    WITHDRAWAL(
        textResId = R.string.withdrawal,
        checkBoxColor = RedLight,
    ),
    INTEREST_POSTING(
        textResId = R.string.interest_posting,
        checkBoxColor = GreenSuccess,
    ),
}
