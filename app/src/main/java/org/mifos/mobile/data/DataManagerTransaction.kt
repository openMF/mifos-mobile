package org.mifos.mobile.data

import android.database.Observable
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManagerTransaction @Inject constructor(private val paymentHubApiManager: PaymentHubApiManager) {
    fun transaction(transaction: Transaction): Observable<TransactionInfo> {
        return paymentHubApiManager.getTransactionsApi().makeTransaction(transaction)
    }
}