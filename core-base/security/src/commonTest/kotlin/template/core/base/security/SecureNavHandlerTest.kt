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
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SecureNavHandlerTest {

    private val handler = SecureNavHandler(DeepLinkValidator())

    @Test
    fun httpsLinkIsSafeByDefault() {
        assertTrue(handler.isDeepLinkSafe("https://example.com/path"))
    }

    @Test
    fun httpLinkIsRejectedByDefault() {
        assertFalse(handler.isDeepLinkSafe("http://example.com/path"))
    }

    @Test
    fun customSchemeIsRejectedByDefault() {
        assertFalse(handler.isDeepLinkSafe("myapp://callback"))
    }

    @Test
    fun sanitizeDeepLinkReturnsUriForSafeLinks() {
        val uri = "https://api.mifos.org/v1/data"
        assertEquals(uri, handler.sanitizeDeepLink(uri))
    }

    @Test
    fun sanitizeDeepLinkReturnsNullForRejectedLinks() {
        assertNull(handler.sanitizeDeepLink("http://evil.com/steal"))
    }
}
