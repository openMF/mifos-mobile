package org.mifos.mobile.core.data.repositories


import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.Transaction

interface RecentTransactionRepository {

    suspend fun recentTransactions(
        offset: Int?,
        limit: Int?
    ): Flow<Page<Transaction>>
}