package org.mifos.selfserviceapp.api.services;

import org.mifos.selfserviceapp.api.ApiEndPoints;
import org.mifos.selfserviceapp.models.Page;
import org.mifos.selfserviceapp.models.Transaction;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Vishwajeet
 * @since 10/08/2016
 */
public interface RecentTransactionsService {
    @GET(ApiEndPoints.CLIENTS + "/{clientId}/transactions")
    Observable<Page<Transaction>> getRecentTransactionsList(
            @Path("clientId") long clientId,
            @Query("offset") int offset,
            @Query("limit") int limit);
}