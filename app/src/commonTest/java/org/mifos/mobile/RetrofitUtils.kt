package org.mifos.mobile

import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

/**
 * Created by dilpreet on 29/7/17.
 */
object RetrofitUtils {
    fun get404Exception(): Exception {
        return HttpException(Response.error<Any>(404, ResponseBody.create(MediaType.parse("application/json"), "Not Found")))
    }

    fun get401Exception(): Exception {
        return HttpException(Response.error<Any>(401, ResponseBody.create(MediaType.parse("application/json"), "UnAuthorized")))
    }
}