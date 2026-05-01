/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.security

import co.touchlab.kermit.Logger

/**
 * Validates deep link URIs against a whitelist of allowed schemes and hosts
 * to prevent open-redirect and injection attacks.
 *
 * Consumer apps register their allowed patterns during initialization.
 */
class DeepLinkValidator(
    private val allowedSchemes: Set<String> = setOf("https"),
    private val allowedHosts: Set<String> = emptySet(),
) {

    /**
     * Validate a deep link URI. Returns true only if the scheme and host
     * are in the allowed sets.
     *
     * @param uri The raw URI string to validate.
     */
    fun isValid(uri: String): Boolean {
        val scheme = uri.substringBefore("://", "").lowercase()
        val schemeValid = scheme in allowedSchemes
        if (!schemeValid) {
            Logger.w("DeepLinkValidator") { "Rejected scheme: $scheme" }
        }

        val hostValid = if (schemeValid && allowedHosts.isNotEmpty()) {
            val hostPart = uri.substringAfter("://", "").substringBefore("/").substringBefore("?")
            val host = hostPart.substringBefore(":").lowercase()
            val allowed = host in allowedHosts
            if (!allowed) Logger.w("DeepLinkValidator") { "Rejected host: $host" }
            allowed
        } else {
            true
        }

        return schemeValid && hostValid
    }
}
