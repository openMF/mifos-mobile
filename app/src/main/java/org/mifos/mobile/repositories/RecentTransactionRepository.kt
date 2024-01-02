package org.mifos.mobile.repositories


import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.Transaction

interface RecentTransactionRepository {

    suspend fun recentTransactions(
        offset: Int?,
        limit: Int?
    ): Flow<Page<Transaction>>
}