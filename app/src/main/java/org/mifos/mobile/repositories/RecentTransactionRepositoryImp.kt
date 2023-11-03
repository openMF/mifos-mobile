package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.Transaction
import javax.inject.Inject

class RecentTransactionRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    RecentTransactionRepository {

    override suspend fun recentTransactions(
        offset: Int?,
        limit: Int?
    ): Flow<Page<Transaction>> {
        return flow {
            offset?.let { limit?.let { it1 -> dataManager.getRecentTransactions(it, it1) } }
                ?.let { emit(it) }
        }
    }
}