package org.mifos.mobile.repositories


import io.reactivex.Observable
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.Transaction

interface RecentTransactionRepository {

    fun recentTransactions(
        offset: Int?,
        limit: Int?
    ): Observable<Page<Transaction?>?>?
}