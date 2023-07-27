package org.mifos.mobile.repositories

import io.reactivex.Observable
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.Transaction
import javax.inject.Inject

class RecentTransactionRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    RecentTransactionRepository {

    override fun recentTransactions(offset: Int?, limit: Int?): Observable<Page<Transaction?>?>? {
        return limit?.let { offset?.let { it1 -> dataManager.getRecentTransactions(it1, it) } }
    }
}