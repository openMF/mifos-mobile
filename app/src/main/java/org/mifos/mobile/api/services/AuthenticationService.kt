package org.mifos.mobile.api.services

import org.mifos.mobile.api.ApiEndPoints
import org.mifos.mobile.models.User
import org.mifos.mobile.models.payload.LoginPayload
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author Vishwajeet
 * @since 09/06/16
 */

interface AuthenticationService {

    @POST(ApiEndPoints.AUTHENTICATION)
    suspend fun authenticate(@Body loginPayload: LoginPayload?): User
}
