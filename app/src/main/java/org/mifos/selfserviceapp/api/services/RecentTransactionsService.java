package org.mifos.selfserviceapp.api.services;

import org.mifos.selfserviceapp.api.ApiEndPoints;
import org.mifos.selfserviceapp.models.TransactionsListResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author Vishwajeet
 * @since 10/08/2016
 */
public interface RecentTransactionsService {
    @GET(ApiEndPoints.CLIENTS + "/{clientId}/transactions")
    Observable<TransactionsListResponse> getRecentTransactionsList(@Path("clientId") long clientId);
}