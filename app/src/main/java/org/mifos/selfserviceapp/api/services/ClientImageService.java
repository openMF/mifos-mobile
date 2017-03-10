package org.mifos.selfserviceapp.api.services;

import org.mifos.selfserviceapp.api.ApiEndPoints;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by f390 on 7/3/17.
 */

public interface ClientImageService {
    @GET(ApiEndPoints.CLIENTS + "/{clientId}/images")
    Call<ResponseBody> getClientImage(@Path("clientId") long clientId);
}
