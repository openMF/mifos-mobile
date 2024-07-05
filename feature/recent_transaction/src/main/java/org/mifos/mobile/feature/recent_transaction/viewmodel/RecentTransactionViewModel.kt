package org.mifos.mobile.feature.recent_transaction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repositories.RecentTransactionRepository
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.feature.recent_transaction.utils.RecentTransactionState
import javax.inject.Inject

@HiltViewModel
class RecentTransactionViewModel @Inject constructor(private val recentTransactionRepositoryImp: RecentTransactionRepository) :
    ViewModel() {

    private val limit = 50
    private var transactions: MutableList<Transaction> = mutableListOf()

    private val _recentTransactionUiState = MutableStateFlow<RecentTransactionState>(RecentTransactionState.Loading)

    val recentTransactionUiState: StateFlow<RecentTransactionState> = _recentTransactionUiState

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
        _recentTransactionUiState.value = RecentTransactionState.Loading
        loadRecentTransactions(0)
    }

    private fun loadRecentTransactions(offset: Int) {
        viewModelScope.launch {
            recentTransactionRepositoryImp.recentTransactions(offset, limit)
                .catch {
                    _recentTransactionUiState.value = RecentTransactionState.Error(it.message)
                }
                .collect {
                    transactions.plus(it.pageItems)
                    _recentTransactionUiState.value = RecentTransactionState.Success(
                        transactions = transactions,
                        canPaginate = it.pageItems.isNotEmpty()
                    )
                    _isPaginating.emit(false)
                    _isRefreshing.emit(false)
                }
        }
    }

}
