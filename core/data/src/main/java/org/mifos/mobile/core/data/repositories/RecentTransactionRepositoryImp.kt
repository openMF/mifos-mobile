package org.mifos.mobile.core.data.repositories

import org.mifos.mobile.core.network.DataManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.Transaction
import javax.inject.Inject

class RecentTransactionRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    RecentTransactionRepository {

    override suspend fun recentTransactions(
        offset: Int?,
        limit: Int?
    ): Flow<Page<Transaction>> {
        return flow {
            if(offset != null && limit != null)
                emit(dataManager.getRecentTransactions(offset = offset, limit = limit))
        }
    }
}