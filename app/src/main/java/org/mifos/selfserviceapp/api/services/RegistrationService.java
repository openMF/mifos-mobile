package org.mifos.selfserviceapp.api.services;

import org.mifos.selfserviceapp.api.ApiEndPoints;
import org.mifos.selfserviceapp.models.register.RegisterPayload;
import org.mifos.selfserviceapp.models.register.UserVerify;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by dilpreet on 31/7/17.
 */

public interface RegistrationService {

    @POST(ApiEndPoints.REGISTRATION)
    Observable<ResponseBody> registerUser(@Body RegisterPayload registerPayload);

    @POST(ApiEndPoints.REGISTRATION + "/user")
    Observable<ResponseBody> verifyUser(@Body UserVerify userVerify);
}
