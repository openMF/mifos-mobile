package org.mifos.mobile.core.network.services

import org.mifos.mobile.core.model.entity.User
import org.mifos.mobile.core.model.entity.payload.LoginPayload
import org.mifos.mobile.core.common.ApiEndPoints
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
