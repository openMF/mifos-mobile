package org.mifos.mobile.ui.recent_transactions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.models.Transaction
import org.mifos.mobile.models.client.Type
import org.mifos.mobile.repositories.RecentTransactionRepository
import javax.inject.Inject

@HiltViewModel
class RecentTransactionViewModel @Inject constructor(private val recentTransactionRepositoryImp: RecentTransactionRepository) :
    ViewModel() {

    private val limit = 50
    private var transactions: MutableList<Transaction> = mutableListOf()

    private val _recentTransactionUiState = MutableStateFlow<RecentTransactionUiState>(RecentTransactionUiState.Loading)
    val recentTransactionUiState: StateFlow<RecentTransactionUiState> = _recentTransactionUiState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    private val _isPaginating = MutableStateFlow(false)
    val isPaginating: StateFlow<Boolean> get() = _isPaginating.asStateFlow()

    fun refresh() {
        _isRefreshing.value = true
        loadInitialTransactions()
    }

    fun loadPaginatedTransactions(offset: Int) {
        _isPaginating.value = true
        transactions.clear()
        loadRecentTransactions(offset)
    }

    fun loadInitialTransactions() {
        _recentTransactionUiState.value = RecentTransactionUiState.Loading
        loadRecentTransactions(0)
    }

    private fun loadRecentTransactions(offset: Int) {
        viewModelScope.launch {
            recentTransactionRepositoryImp.recentTransactions(offset, limit)
                .catch {
                    _recentTransactionUiState.value = RecentTransactionUiState.Error(it.message)
                }
                .collect {
                    transactions.plus(it.pageItems)
                    _recentTransactionUiState.value = RecentTransactionUiState.Success(transactions = transactions, canPaginate = it.pageItems.isNotEmpty())
                    _isPaginating.emit(false)
                    _isRefreshing.emit(false)
                }
        }
    }

}

sealed class RecentTransactionUiState {
    data object Loading : RecentTransactionUiState()
    data class Error(val message: String?) : RecentTransactionUiState()
    data class Success(val transactions: List<Transaction>, val canPaginate: Boolean) : RecentTransactionUiState()
}
