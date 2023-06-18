package org.mifos.mobile.utils

import org.mifos.mobile.models.Transaction


sealed class TransactionLoadingState {
    object ShowProgress : TransactionLoadingState()
    object HideProgress : TransactionLoadingState()
    data class RecentTransactions(val transactions: List<Transaction>) : TransactionLoadingState()
    data class LoadMoreTransactions(val transactions: List<Transaction>) : TransactionLoadingState()
    object EmptyTransaction : TransactionLoadingState()
    data class ErrorFetchingTransactions(val errorMessageResId: Int) : TransactionLoadingState()
}
