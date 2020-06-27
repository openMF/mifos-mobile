package org.mifos.mobile.api.services

import io.reactivex.Observable
import org.mifos.mobile.api.ApiEndPoints
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.Transaction
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by shivangi on 27/June/2020
 */
interface RecentTransactionsService {
    @GET(ApiEndPoints.CLIENTS + "/{clientId}/transactions")
    fun getRecentTransactionsList(
            @Path("clientId") clientId: Long,
            @Query("offset") offset: Int,
            @Query("limit") limit: Int): Observable<Page<Transaction?>?>?
}