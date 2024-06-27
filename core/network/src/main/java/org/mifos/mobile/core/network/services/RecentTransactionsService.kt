package org.mifos.mobile.core.network.services

import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.common.ApiEndPoints
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Vishwajeet
 * @since 10/08/2016
 */
interface RecentTransactionsService {
    @GET(ApiEndPoints.CLIENTS + "/{clientId}/transactions")
    suspend fun getRecentTransactionsList(
        @Path("clientId") clientId: Long?,
        @Query("offset") offset: Int?,
        @Query("limit") limit: Int?,
    ): Page<Transaction>
}
