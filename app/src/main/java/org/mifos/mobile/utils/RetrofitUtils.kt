package org.mifos.mobile.utils

import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

/**
 * Created by dilpreet on 29/7/17.
 */
object RetrofitUtils {
    fun getResponseForError(errorCode: Int): Exception {
        val message = if (errorCode == 401) "UnAuthorized" else "Not Found"
        val responseBody =
            ResponseBody.create(MediaType.parse("application/json"), "{\"message\":\"$message\"}")
        val response = okhttp3.Response.Builder().code(errorCode)
            .message(message)
            .protocol(Protocol.HTTP_1_1)
            .request(okhttp3.Request.Builder().url("http://localhost/").build())
            .build()
        return HttpException(Response.error<Any>(responseBody, response))
    }
}
