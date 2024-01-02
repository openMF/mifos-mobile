package org.mifos.mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.repositories.RecentTransactionRepository
import org.mifos.mobile.utils.RecentTransactionUiState
import javax.inject.Inject

@HiltViewModel
class RecentTransactionViewModel @Inject constructor(private val recentTransactionRepositoryImp: RecentTransactionRepository) :
    ViewModel() {

    private val limit = 50
    private var loadmore = false

    private val _recentTransactionUiState =
        MutableStateFlow<RecentTransactionUiState>(RecentTransactionUiState.Initial)
    val recentTransactionUiState: StateFlow<RecentTransactionUiState> = _recentTransactionUiState

    fun loadRecentTransactions(loadmore: Boolean, offset: Int) {
        this.loadmore = loadmore
        loadRecentTransactions(offset, limit)
    }

    private fun loadRecentTransactions(offset: Int, limit: Int) {
        viewModelScope.launch { <<<<<<< HEAD
            try {
                _recentTransactionUiState.value = RecentTransactionUiState.Loading
                val response = recentTransactionRepositoryImp.recentTransactions(offset, limit)
                if (response?.isSuccessful == true) {
                    if (response.body()?.totalFilteredRecords == 0) {
                        _recentTransactionUiState.value = RecentTransactionUiState.EmptyTransaction
                    } else if (loadmore && response.body()?.pageItems?.isNotEmpty() == true) {
                        _recentTransactionUiState.value =
                            RecentTransactionUiState.LoadMoreRecentTransactions(
                                response.body()!!.pageItems
                            )
                    } else if (response.body()?.pageItems?.isNotEmpty() == true) {
                        _recentTransactionUiState.value =
                            RecentTransactionUiState.RecentTransactions(
                                response.body()?.pageItems!!
                            )
                    }
                }
            } catch(e: Exception){
=======

            _recentTransactionUiState.value = RecentTransactionUiState.Loading
            recentTransactionRepositoryImp.recentTransactions(offset, limit).catch {
>>>>>>> e6a6d7b05ee77dc5164d7f8d4abd6225b433b09c
                _recentTransactionUiState.value =
                    RecentTransactionUiState.Error(R.string.recent_transactions)
            }.collect {
                if (it.totalFilteredRecords == 0) {
                    _recentTransactionUiState.value = RecentTransactionUiState.EmptyTransaction
                } else if (loadmore && it.pageItems.isNotEmpty()) {
                    _recentTransactionUiState.value =
                        RecentTransactionUiState.LoadMoreRecentTransactions(it.pageItems)
                } else if (it.pageItems.isNotEmpty()) {
                    _recentTransactionUiState.value =
                        RecentTransactionUiState.RecentTransactions(it.pageItems)
                }
            }
        }
    }

}