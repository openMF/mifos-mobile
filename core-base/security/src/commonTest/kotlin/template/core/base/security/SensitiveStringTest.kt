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
import kotlin.test.assertNotEquals

class SensitiveStringTest {

    @Test
    fun valueReturnsOriginalString() {
        val sensitive = SensitiveString.fromString("secret123")
        assertEquals("secret123", sensitive.value())
    }

    @Test
    fun closeZerosBackingArray() {
        val sensitive = SensitiveString.fromString("secret123")
        sensitive.close()
        assertEquals("\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000", sensitive.value())
    }

    @Test
    fun toStringIsRedacted() {
        val sensitive = SensitiveString.fromString("secret123")
        assertEquals("SensitiveString[REDACTED]", sensitive.toString())
    }

    @Test
    fun equalityWorksBeforeClose() {
        val a = SensitiveString.fromString("test")
        val b = SensitiveString.fromString("test")
        assertEquals(a, b)
    }

    @Test
    fun equalityFailsAfterClose() {
        val a = SensitiveString.fromString("test")
        val b = SensitiveString.fromString("test")
        a.close()
        assertNotEquals(a, b)
    }
}
