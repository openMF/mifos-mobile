package org.mifos.mobile.data

import io.reactivex.Observable
import org.mifos.mobile.models.entity.Transaction
import org.mifos.mobile.models.entity.TransactionInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManagerTransaction @Inject constructor(private val paymentHubApiManager: PaymentHubApiManager) {
    fun transaction(transaction: Transaction): Observable<TransactionInfo> {
        return paymentHubApiManager.getTransactionsApi().makeTransaction(transaction)
    }
}