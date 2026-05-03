/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.CertificatePinner
import template.core.base.security.CertificatePinConfig

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) {
    config(this)
}

/**
 * Creates an OkHttp-based [HttpClient] with TLS certificate pinning applied.
 */
fun httpClient(
    pinConfig: CertificatePinConfig,
    config: HttpClientConfig<*>.() -> Unit,
) = HttpClient(OkHttp) {
    if (pinConfig.pins.isNotEmpty()) {
        engine {
            config {
                certificatePinner(
                    CertificatePinner.Builder().apply {
                        pinConfig.pins.forEach { (host, hashes) ->
                            hashes.forEach { hash -> add(host, "sha256/$hash") }
                        }
                    }.build(),
                )
            }
        }
    }
    config(this)
}
