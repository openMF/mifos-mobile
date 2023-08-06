package org.mifos.mobile.repositories


import org.mifos.mobile.models.Page
import org.mifos.mobile.models.Transaction
import retrofit2.Response

interface RecentTransactionRepository {

    suspend fun recentTransactions(
        offset: Int?,
        limit: Int?
    ): Response<Page<Transaction?>?>?
}