package org.mifos.mobile.api.services

import okhttp3.ResponseBody
import org.mifos.mobile.api.ApiEndPoints
import org.mifos.mobile.models.UpdatePasswordPayload
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

/*
* Created by saksham on 13/July/2018
*/ interface UserDetailsService {
    @PUT(ApiEndPoints.USER)
    suspend fun updateAccountPassword(@Body payload: UpdatePasswordPayload?): Response<ResponseBody?>?
}
