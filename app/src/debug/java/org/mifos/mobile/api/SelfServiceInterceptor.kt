/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package org.mifos.mobile.api

import android.text.TextUtils
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import org.mifos.mobile.api.local.PreferencesHelper
import java.io.IOException

/**
 * @author Vishwajeet
 * @since 21/06/16
 */
class SelfServiceInterceptor(private val preferencesHelper: PreferencesHelper) :
    Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val chainRequest = chain.request()
        val builder = chainRequest.newBuilder()
            .header(HEADER_TENANT, preferencesHelper.tenant!!)
            .header(CONTENT_TYPE, "application/json")
        if (!TextUtils.isEmpty(preferencesHelper.token)) {
            builder.header(HEADER_AUTH, preferencesHelper.token!!)
        }
        val request = builder.build()
        return chain.proceed(request)
    }

    companion object {
        const val HEADER_TENANT = "Fineract-Platform-TenantId"
        const val HEADER_AUTH = "Authorization"
        const val DEFAULT_TENANT = "gsoc"
        const val CONTENT_TYPE = "Content-Type"
    }
}
