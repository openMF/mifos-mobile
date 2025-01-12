/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.utils

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.util.AttributeKey

class KtorInterceptor(
    private val getToken: () -> String?,
) {
    companion object Plugin : HttpClientPlugin<Config, KtorInterceptor> {
        private const val HEADER_TENANT = "Fineract-Platform-TenantId"
        private const val HEADER_AUTH = "Authorization"
        private const val DEFAULT = "default"
        private const val CONTENT_TYPE = "Content-Type"
        override val key: AttributeKey<KtorInterceptor> = AttributeKey("KtorInterceptor")

        override fun install(plugin: KtorInterceptor, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                context.header(CONTENT_TYPE, "application/json")
                context.header("Accept", "application/json")
                context.header(HEADER_TENANT, DEFAULT)

                plugin.getToken()?.let { token ->
                    if (token.isNotEmpty()) {
                        context.headers[HEADER_AUTH] = "Basic $token"
                    }
                }
            }
        }

        override fun prepare(block: Config.() -> Unit): KtorInterceptor {
            val config = Config().apply(block)
            return KtorInterceptor(config.getToken)
        }
    }
}

class Config {
    lateinit var getToken: () -> String?
}

class KtorInterceptorRe(
    private val preferencesHelper: PreferencesHelper,
) {
    companion object Plugin : HttpClientPlugin<ConfigRe, KtorInterceptorRe> {
        private const val HEADER_TENANT = "Fineract-Platform-TenantId"
        private const val HEADER_AUTH = "Authorization"
        private const val DEFAULT = "venus"
        private const val CONTENT_TYPE = "Content-Type"
        override val key: AttributeKey<KtorInterceptorRe> = AttributeKey("KtorInterceptorRe")

        override fun install(plugin: KtorInterceptorRe, scope: HttpClient) {
            val token = plugin.repository.token.value

            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                context.header(CONTENT_TYPE, "application/json")
                context.header("Accept", "application/json")
                context.header(HEADER_TENANT, DEFAULT)

                token?.let { token ->
                    if (token.isNotEmpty()) {
                        context.headers[HEADER_AUTH] = "Basic $token"
                    }
                }
            }
        }

        override fun prepare(block: ConfigRe.() -> Unit): KtorInterceptorRe {
            val config = ConfigRe().apply(block)
            return KtorInterceptorRe(config.repository)
        }
    }
}

class ConfigRe {
    lateinit var preferencesHelper: PreferencesHelper
}
