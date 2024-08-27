/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.datastore

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Request.Builder
import okhttp3.Response
import java.io.IOException

/**
 * @author Vishwajeet
 * @since 21/06/16
 */
class SelfServiceInterceptor(private val preferencesHelper: PreferencesHelper) :
    Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val chainRequest: Request = chain.request()
        val builder: Builder = chainRequest.newBuilder()

        preferencesHelper.tenant?.let {
            builder.header(HEADER_TENANT, it)
        }
        preferencesHelper.token?.let {
            if (it.isNotEmpty()) {
                builder.header(HEADER_AUTH, it)
            }
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
