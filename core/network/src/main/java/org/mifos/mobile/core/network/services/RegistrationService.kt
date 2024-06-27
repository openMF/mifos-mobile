package org.mifos.mobile.core.network.services

import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.register.RegisterPayload
import org.mifos.mobile.core.model.entity.register.UserVerify
import org.mifos.mobile.core.common.ApiEndPoints
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by dilpreet on 31/7/17.
 */
interface RegistrationService {
    @POST(ApiEndPoints.REGISTRATION)
    suspend fun registerUser(@Body registerPayload: RegisterPayload?): ResponseBody

    @POST(ApiEndPoints.REGISTRATION + "/user")
    suspend fun verifyUser(@Body userVerify: UserVerify?): ResponseBody
}
