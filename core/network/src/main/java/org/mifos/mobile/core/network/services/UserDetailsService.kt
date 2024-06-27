package org.mifos.mobile.core.network.services

import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.UpdatePasswordPayload
import org.mifos.mobile.core.common.ApiEndPoints
import retrofit2.http.Body
import retrofit2.http.PUT

/*
* Created by saksham on 13/July/2018
*/ interface UserDetailsService {
    @PUT(ApiEndPoints.USER)
    suspend fun updateAccountPassword(@Body payload: UpdatePasswordPayload?): ResponseBody
}
