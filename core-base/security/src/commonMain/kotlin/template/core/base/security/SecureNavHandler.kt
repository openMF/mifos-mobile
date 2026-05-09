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

/**
 * Deep link security handler wrapping [DeepLinkValidator].
 *
 * Provides a clean API for navigation code to validate incoming
 * deep links before processing them. Default configuration allows
 * only `https` scheme; consumer apps can override the
 * [DeepLinkValidator] Koin binding to add custom schemes or hosts.
 */
class SecureNavHandler(
    private val validator: DeepLinkValidator,
) {
    /** Returns true if the deep link URI passes validation. */
    fun isDeepLinkSafe(uri: String): Boolean = validator.isValid(uri)

    /**
     * Returns the URI if it passes validation, or null if rejected.
     * Useful for safe navigation: `secureNavHandler.sanitizeDeepLink(uri)?.let { navigate(it) }`
     */
    fun sanitizeDeepLink(uri: String): String? =
        if (validator.isValid(uri)) uri else null
}
