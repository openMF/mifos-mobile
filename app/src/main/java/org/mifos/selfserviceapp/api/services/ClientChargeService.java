package org.mifos.selfserviceapp.api.services;

import org.mifos.selfserviceapp.api.ApiEndPoints;
import org.mifos.selfserviceapp.models.ChargeListResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author Vishwajeet
 * @since 17/8/16.
 */
public interface ClientChargeService {
    @GET(ApiEndPoints.CLIENTS + "/{clientId}/charges")
    Observable<ChargeListResponse> getClientChargeList(@Path("clientId") long clientId);
}
