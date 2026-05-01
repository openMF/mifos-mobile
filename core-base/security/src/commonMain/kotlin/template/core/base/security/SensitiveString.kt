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
 * Zeroable credential wrapper backed by [CharArray] instead of [String].
 *
 * JVM String is immutable and can linger in memory. [SensitiveString]
 * allows explicit zeroing after use to minimize exposure window.
 */
class SensitiveString(private val chars: CharArray) : AutoCloseable {

    /** Read the value. Call [close] when done. */
    fun value(): String = chars.concatToString()

    /** Zero out the backing array, making the value unrecoverable. */
    override fun close() {
        chars.fill('\u0000')
    }

    /** Prevent accidental logging. */
    override fun toString(): String = "SensitiveString[REDACTED]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SensitiveString) return false
        return chars.contentEquals(other.chars)
    }

    override fun hashCode(): Int = chars.contentHashCode()

    companion object {
        fun fromString(value: String): SensitiveString =
            SensitiveString(value.toCharArray())
    }
}
