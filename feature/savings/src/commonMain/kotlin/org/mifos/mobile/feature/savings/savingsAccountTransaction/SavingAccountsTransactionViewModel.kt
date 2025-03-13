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

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mifos_mobile.feature.savings.generated.resources.Res
import mifos_mobile.feature.savings.generated.resources.date
import mifos_mobile.feature.savings.generated.resources.deposit
import mifos_mobile.feature.savings.generated.resources.dividend_payout
import mifos_mobile.feature.savings.generated.resources.four_weeks
import mifos_mobile.feature.savings.generated.resources.interest_posting
import mifos_mobile.feature.savings.generated.resources.six_months
import mifos_mobile.feature.savings.generated.resources.three_months
import mifos_mobile.feature.savings.generated.resources.triangular_green_view
import mifos_mobile.feature.savings.generated.resources.triangular_red_view
import mifos_mobile.feature.savings.generated.resources.withdrawal
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants.SAVINGS_ID
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.entity.accounts.savings.TransactionType
import org.mifos.mobile.core.model.entity.accounts.savings.Transactions
import org.mifos.mobile.feature.savings.savingsAccountTransaction.SavingsAccountTransactionUiState.Loading

internal class SavingAccountsTransactionViewModel(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle,
    networkMonitor: NetworkMonitor,
) : ViewModel() {

    val isNetworkAvailable = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

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
                    dataState ->
                when (dataState) {
                    is DataState.Error -> {
                        mUiState.value = SavingsAccountTransactionUiState.Error(dataState.message)
                    }
                    DataState.Loading -> {
                        mUiState.value = Loading
                    }
                    is DataState.Success -> {
                        val savingsWithAssociations = dataState.data
                        _transactionsList = savingsWithAssociations.transactions

                        mUiState.value = when {
                            savingsWithAssociations.transactions.isEmpty() -> SavingsAccountTransactionUiState.Empty
                            else -> SavingsAccountTransactionUiState.Success(savingsWithAssociations.transactions)
                        }
                    }
                }
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
    data object Empty : SavingsAccountTransactionUiState()
    data class Error(val errorMessage: String?) : SavingsAccountTransactionUiState()
    data class Success(val savingAccountsTransactionList: List<Transactions>) :
        SavingsAccountTransactionUiState()
}

internal fun getTransactionTriangleResId(transactionType: TransactionType?): DrawableResource {
    return transactionType?.run {
        when {
            deposit == true -> Res.drawable.triangular_green_view
            dividendPayout == true -> Res.drawable.triangular_red_view
            withdrawal == true -> Res.drawable.triangular_red_view
            interestPosting == true -> Res.drawable.triangular_green_view
            feeDeduction == true -> Res.drawable.triangular_red_view
            initiateTransfer == true -> Res.drawable.triangular_red_view
            approveTransfer == true -> Res.drawable.triangular_red_view
            withdrawTransfer == true -> Res.drawable.triangular_red_view
            rejectTransfer == true -> Res.drawable.triangular_green_view
            overdraftFee == true -> Res.drawable.triangular_red_view
            else -> Res.drawable.triangular_green_view
        }
    } ?: Res.drawable.triangular_red_view
}

data class SavingsTransactionFilterDataModel(
    val startDate: Long,
    val endDate: Long,
    val radioFilter: SavingsTransactionRadioFilter?,
    val checkBoxFilters: MutableList<SavingsTransactionCheckBoxFilter>,
)

val SavingsTransactionFilterDataModelSaver: Saver<SavingsTransactionFilterDataModel, Any> =
    listSaver(
        save = {
            listOf(
                it.startDate,
                it.endDate,
                it.radioFilter?.name,
                it.checkBoxFilters.map { filter -> filter.name }.toList(),
            )
        },
        restore = {
            SavingsTransactionFilterDataModel(
                startDate = it[0] as Long,
                endDate = it[1] as Long,
                radioFilter = (it[2] as? String)?.let { name ->
                    runCatching { SavingsTransactionRadioFilter.valueOf(name) }.getOrNull()
                },
                checkBoxFilters = (it[3] as? List<*>)?.mapNotNull { name ->
                    (name as? String)?.let { n ->
                        runCatching { SavingsTransactionCheckBoxFilter.valueOf(n) }.getOrNull()
                    }
                }?.toMutableList() ?: mutableListOf(),
            )
        },
    )

enum class SavingsTransactionRadioFilter(val textResId: StringResource) {
    DATE(textResId = Res.string.date),
    FOUR_WEEKS(textResId = Res.string.four_weeks),
    THREE_MONTHS(textResId = Res.string.three_months),
    SIX_MONTHS(textResId = Res.string.six_months),
}

enum class SavingsTransactionCheckBoxFilter(
    val textResId: StringResource,
    val checkBoxColor: @Composable (ColorScheme) -> Color,
) {
    DEPOSIT(
        textResId = Res.string.deposit,
        checkBoxColor = { it.primary },
    ),
    DIVIDEND_PAYOUT(
        textResId = Res.string.dividend_payout,
        checkBoxColor = { it.errorContainer },
    ),
    WITHDRAWAL(
        textResId = Res.string.withdrawal,
        checkBoxColor = { it.errorContainer },
    ),
    INTEREST_POSTING(
        textResId = Res.string.interest_posting,
        checkBoxColor = { it.secondaryContainer },
    ),
}
