package org.mifos.mobile.api.services

import org.mifos.mobile.api.ApiEndPoints
import org.mifos.mobile.models.User
import org.mifos.mobile.models.payload.LoginPayload

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author Vishwajeet
 * @since 09/06/16
 */

interface AuthenticationService {

    @POST(ApiEndPoints.AUTHENTICATION)
    fun authenticate(@Body loginPayload: LoginPayload?): Observable<User?>?
}