package org.mifos.selfserviceapp.api.services;

import org.mifos.selfserviceapp.api.ApiEndPoints;
import org.mifos.selfserviceapp.models.User;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Vishwajeet
 * @since 09/06/16
 */

public interface AuthenticationService {

    @POST(ApiEndPoints.AUTHENTICATION)
    Observable<User> authenticate(@Query("username") String username,
            @Query("password") String password);
}
