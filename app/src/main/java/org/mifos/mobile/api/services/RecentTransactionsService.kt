package org.mifos.mobile.api.services

import org.mifos.mobile.api.ApiEndPoints
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.Transaction
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
