package org.mifos.mobile.utils

import org.mifos.mobile.models.Transaction

sealed class RecentTransactionUiState {
    object Loading : RecentTransactionUiState()
    object EmptyTransaction : RecentTransactionUiState()
    data class Error(val message: Int) : RecentTransactionUiState()
    data class RecentTransactions(val transactions: List<Transaction?>) : RecentTransactionUiState()
    data class LoadMoreRecentTransactions(val transactions: List<Transaction?>) : RecentTransactionUiState()
}
