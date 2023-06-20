package org.mifos.mobile.utils

import org.mifos.mobile.models.Transaction


sealed class TransactionUiState {
    data class Loading(val showProgress: Boolean) : TransactionUiState()
    data class RecentTransactions(val transactions: List<Transaction>) : TransactionUiState()
    data class LoadMoreTransactions(val transactions: List<Transaction>) : TransactionUiState()
    object EmptyTransaction : TransactionUiState()
    data class ErrorFetchingTransactions(val errorMessageResId: Int) : TransactionUiState()

    companion object {
        val SHOW_PROGRESS = Loading(true)
        val HIDE_PROGRESS = Loading(false)
    }
}

