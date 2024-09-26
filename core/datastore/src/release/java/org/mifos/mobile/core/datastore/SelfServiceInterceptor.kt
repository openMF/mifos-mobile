/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.datastore

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Request.Builder
import okhttp3.Response
import java.io.IOException

class SelfServiceInterceptor(private val preferencesHelper: PreferencesHelper) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val chainRequest: Request = chain.request()
        val builder: Builder = chainRequest.newBuilder()
        builder.header("Content-Type", "application/json")
        builder.header("Accept", "application/json")

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
        const val DEFAULT_TENANT = "gsoc"
    }
}
