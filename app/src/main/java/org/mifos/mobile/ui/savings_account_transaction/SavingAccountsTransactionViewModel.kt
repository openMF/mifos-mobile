package org.mifos.mobile.ui.savings_account_transaction


import android.os.Parcelable
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.DepositGreen
import org.mifos.mobile.core.ui.theme.GreenSuccess
import org.mifos.mobile.core.ui.theme.RedLight
import org.mifos.mobile.models.accounts.savings.TransactionType
import org.mifos.mobile.models.accounts.savings.Transactions
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.DateHelper
import javax.inject.Inject

@HiltViewModel
class SavingAccountsTransactionViewModel @Inject constructor(private val savingsAccountRepositoryImp: SavingsAccountRepository) :
    ViewModel() {

    private val _savingAccountsTransactionUiState = MutableStateFlow<SavingsAccountTransactionUiState>(SavingsAccountTransactionUiState.Loading)
    val savingAccountsTransactionUiState: StateFlow<SavingsAccountTransactionUiState> get() = _savingAccountsTransactionUiState

    private var _transactionsList: List<Transactions> = mutableListOf()
    private val transactionsList: List<Transactions> get() = _transactionsList

    private var _savingsId: Long = 0
    val savingsId get() = _savingsId

    fun setSavingsId(savingsId: Long) {
        _savingsId = savingsId
        loadSavingsWithAssociations(savingsId)
    }

    fun loadSavingsWithAssociations(accountId: Long) {
        viewModelScope.launch {
            _savingAccountsTransactionUiState.value = SavingsAccountTransactionUiState.Loading
            savingsAccountRepositoryImp.getSavingsWithAssociations(
                accountId,
                Constants.TRANSACTIONS,
            ).catch {
                _savingAccountsTransactionUiState.value =
                    SavingsAccountTransactionUiState.Error(it.message)
            }.collect {
                _transactionsList = it.transactions
                _savingAccountsTransactionUiState.value =
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
                    checkBoxFilters = filter.checkBoxFilters
                )
            }
            filter.radioFilter != null -> {
                filterByDate(
                    startDate = filter.startDate,
                    endDate = filter.endDate
                )
            }
            filter.checkBoxFilters.isNotEmpty() -> {
                filterByType(
                    checkBoxFilters = filter.checkBoxFilters
                )
            }
            else -> {
                _savingAccountsTransactionUiState.value = SavingsAccountTransactionUiState.Success(transactionsList)
            }
        }
    }


    private fun filterByDateAndType(
        startDate: Long,
        endDate: Long,
        checkBoxFilters: MutableList<SavingsTransactionCheckBoxFilter>
    ) {
        val typeFilteredList = filterSavingsAccountTransactionsByType(checkBoxFilters = checkBoxFilters)
        val dateAndTypeFilteredList = filterTransactionListByDate(transactions = typeFilteredList, startDate = startDate, endDate = endDate)
        _savingAccountsTransactionUiState.value = SavingsAccountTransactionUiState.Success(dateAndTypeFilteredList)
    }

    private fun filterByDate(startDate: Long, endDate: Long) {
        val list = filterTransactionListByDate(transactions = transactionsList, startDate = startDate, endDate = endDate)
        _savingAccountsTransactionUiState.value = SavingsAccountTransactionUiState.Success(list)
    }

    private fun filterByType(
        checkBoxFilters: MutableList<SavingsTransactionCheckBoxFilter>
    ) {
        val list = filterSavingsAccountTransactionsByType(checkBoxFilters = checkBoxFilters)
        _savingAccountsTransactionUiState.value = SavingsAccountTransactionUiState.Success(list)
    }

    private fun filterTransactionListByDate(
        transactions: List<Transactions>,
        startDate: Long,
        endDate: Long
    ): List<Transactions> {
        return transactions.filter {
            (DateHelper.getDateAsLongFromList(it.date) in startDate..endDate)
        }
    }

    private fun filterSavingsAccountTransactionsByType(
        checkBoxFilters: MutableList<SavingsTransactionCheckBoxFilter>
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

sealed class SavingsAccountTransactionUiState {
    data object Loading : SavingsAccountTransactionUiState()
    data class Error(val errorMessage: String?) : SavingsAccountTransactionUiState()
    data class Success(val savingAccountsTransactionList: List<Transactions>?) : SavingsAccountTransactionUiState()
}

fun getTransactionTriangleResId(transactionType: TransactionType?): Int {
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
data class SavingsTransactionFilterDataModel(
    val startDate: Long,
    val endDate: Long,
    val radioFilter: SavingsTransactionRadioFilter?,
    val checkBoxFilters: MutableList<SavingsTransactionCheckBoxFilter>
): Parcelable

enum class SavingsTransactionRadioFilter(val textResId: Int) {
    DATE(textResId = R.string.date),
    FOUR_WEEKS(textResId = R.string.four_weeks),
    THREE_MONTHS(textResId = R.string.three_months),
    SIX_MONTHS(textResId = R.string.six_months)
}

enum class SavingsTransactionCheckBoxFilter(
    val textResId: Int,
    val checkBoxColor: Color,
) {
    DEPOSIT(
        textResId = R.string.deposit,
        checkBoxColor = DepositGreen
    ),
    DIVIDEND_PAYOUT(
        textResId = R.string.dividend_payout,
        checkBoxColor = RedLight
    ),
    WITHDRAWAL(
        textResId = R.string.withdrawal,
        checkBoxColor = RedLight
    ),
    INTEREST_POSTING(
        textResId = R.string.interest_posting,
        checkBoxColor = GreenSuccess
    )
}

