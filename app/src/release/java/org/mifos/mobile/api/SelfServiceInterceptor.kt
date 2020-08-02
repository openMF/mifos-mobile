/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package org.mifos.mobile.api

import android.text.TextUtils

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Request.Builder
import okhttp3.Response

import kotlin.jvm.Throws

import org.mifos.mobile.api.SelfServiceInterceptor

/**
 * @author Vishwajeet
 * @since 21/06/16
 */
class SelfServiceInterceptor(private val tenant: String?, private val authToken: String?) :
        Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val chainRequest: Request = chain.request()
        val builder: Builder = chainRequest.newBuilder()
                .header(HEADER_TENANT, tenant)
        if (!TextUtils.isEmpty(authToken)) {
            builder.header(HEADER_AUTH, authToken)
        }
        val request: Request = builder.build()
        return chain.proceed(request)
    }

    companion object {
        const val HEADER_TENANT = "Fineract-Platform-TenantId"
        const val HEADER_AUTH = "Authorization"
        const val DEFAULT_TENANT = "mobile"
    }
}