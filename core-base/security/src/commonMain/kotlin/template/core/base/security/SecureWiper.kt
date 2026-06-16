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
 * Securely wipes sensitive data from storage and memory.
 *
 * Used by [FailedAttemptTracker] when the wipe threshold is exceeded
 * and by session management on logout.
 */
expect class SecureWiper() {

    /** Wipe all secure preferences and encryption keys. */
    fun wipeSecureStorage()

    /** Overwrite a byte array with zeros. */
    fun scrubMemory(data: ByteArray)
}
