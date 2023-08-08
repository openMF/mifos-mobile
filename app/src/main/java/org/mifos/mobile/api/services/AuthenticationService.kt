package org.mifos.mobile.api.services

import io.reactivex.Observable
import org.mifos.mobile.api.ApiEndPoints
import org.mifos.mobile.models.User
import org.mifos.mobile.models.payload.LoginPayload
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author Vishwajeet
 * @since 09/06/16
 */

interface AuthenticationService {

    @POST(ApiEndPoints.AUTHENTICATION)
    suspend fun authenticate(@Body loginPayload: LoginPayload?): Response<User?>?
}
