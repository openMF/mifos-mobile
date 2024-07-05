package org.mifos.mobile.feature.recent_transaction.utils

import org.mifos.mobile.core.model.entity.Transaction

sealed class RecentTransactionState {
    data object Loading : RecentTransactionState()
    data class Error(val message: String?) : RecentTransactionState()
    data class Success(val transactions: List<Transaction>, val canPaginate: Boolean) : RecentTransactionState()
}