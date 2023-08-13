package org.mifos.mobile.repositories

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.Transaction
import retrofit2.Response
import javax.inject.Inject

class RecentTransactionRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    RecentTransactionRepository {

    override suspend fun recentTransactions(
        offset: Int?,
        limit: Int?
    ): Response<Page<Transaction?>?>? {
        return limit?.let { offset?.let { it1 -> dataManager.getRecentTransactions(it1, it) } }
    }
}