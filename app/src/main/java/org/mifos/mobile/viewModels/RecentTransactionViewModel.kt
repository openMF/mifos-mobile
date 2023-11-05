package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.repositories.RecentTransactionRepository
import org.mifos.mobile.utils.RecentTransactionUiState
import javax.inject.Inject

@HiltViewModel
class RecentTransactionViewModel @Inject constructor(private val recentTransactionRepository: RecentTransactionRepository) :
    ViewModel() {

    private val limit = 50
    private var loadMore = false

    private val _recentTransactionUiState = MutableLiveData<RecentTransactionUiState>()
    val recentTransactionUiState: LiveData<RecentTransactionUiState> = _recentTransactionUiState

    fun loadRecentTransactions(loadMore: Boolean, offset: Int) {
        this.loadMore = loadMore
        loadRecentTransactions(offset, limit)
    }

    private fun loadRecentTransactions(offset: Int, limit: Int) {
        viewModelScope.launch {
            _recentTransactionUiState.value = RecentTransactionUiState.Loading

            val response = recentTransactionRepository.recentTransactions(offset, limit)

            if (response?.isSuccessful == true) {
                when {
                    response.body()?.totalFilteredRecords == 0 -> {
                        _recentTransactionUiState.value = RecentTransactionUiState.EmptyTransaction
                    }
                    loadMore && response.body()?.pageItems?.isNotEmpty() == true -> {
                        _recentTransactionUiState.value =
                            RecentTransactionUiState.LoadMoreRecentTransactions(response.body()!!.pageItems)
                    }
                    response.body()?.pageItems?.isNotEmpty() == true -> {
                        _recentTransactionUiState.value =
                            RecentTransactionUiState.RecentTransactions(response.body()!!.pageItems)
                    }
                    else -> {
                        _recentTransactionUiState.value =
                            RecentTransactionUiState.Error(R.string.recent_transactions)
                    }
                }
            } else {
                _recentTransactionUiState.value = RecentTransactionUiState.Error(R.string.recent_transactions)
            }
        }
    }
}
