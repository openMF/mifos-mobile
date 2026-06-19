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

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DeepLinkValidatorTest {

    @Test
    fun httpsSchemeIsAllowedByDefault() {
        val validator = DeepLinkValidator()
        assertTrue(validator.isValid("https://example.com/path"))
    }

    @Test
    fun httpSchemeIsRejectedByDefault() {
        val validator = DeepLinkValidator()
        assertFalse(validator.isValid("http://example.com/path"))
    }

    @Test
    fun customSchemeIsRejectedWhenNotWhitelisted() {
        val validator = DeepLinkValidator()
        assertFalse(validator.isValid("myapp://callback"))
    }

    @Test
    fun customSchemeIsAllowedWhenWhitelisted() {
        val validator = DeepLinkValidator(allowedSchemes = setOf("https", "myapp"))
        assertTrue(validator.isValid("myapp://callback"))
    }

    @Test
    fun hostValidationRejectsUnknownHost() {
        val validator = DeepLinkValidator(allowedHosts = setOf("api.mifos.org"))
        assertFalse(validator.isValid("https://evil.com/steal"))
    }

    @Test
    fun hostValidationAcceptsKnownHost() {
        val validator = DeepLinkValidator(allowedHosts = setOf("api.mifos.org"))
        assertTrue(validator.isValid("https://api.mifos.org/v1/data"))
    }

    @Test
    fun emptyHostWhitelistAllowsAllHosts() {
        val validator = DeepLinkValidator(allowedHosts = emptySet())
        assertTrue(validator.isValid("https://any.host.com/path"))
    }

    @Test
    fun invalidUriIsRejected() {
        val validator = DeepLinkValidator()
        assertFalse(validator.isValid("not-a-uri"))
    }
}
