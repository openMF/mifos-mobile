package org.mifos.mobilebanking.api.services;

/*
 * Created by saksham on 13/July/2018
 */

import org.mifos.mobilebanking.api.ApiEndPoints;
import org.mifos.mobilebanking.models.UpdatePasswordPayload;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface UserDetailsService {

    @PUT(ApiEndPoints.USER)
    Observable<ResponseBody> updateAccountPassword(@Body UpdatePasswordPayload payload);
}
