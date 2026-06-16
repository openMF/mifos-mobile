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
 * Platform-specific secure key storage and retrieval.
 *
 * - Android: Android Keystore (hardware-backed)
 * - iOS: Keychain Services
 * - Desktop: OS credential APIs (macOS Keychain, Linux libsecret, Windows DPAPI)
 * - Web: IndexedDB CryptoKey (non-extractable)
 */
expect class SecureKeyProvider {
    /** Retrieves a platform key handle, or null if the key was lost (factory reset, etc.). */
    fun getExistingKey(): Any?

    /** Returns an existing key handle or creates and stores a new one. */
    fun getOrCreateKey(): Any

    /** Deletes the encryption key, making all encrypted data permanently unreadable. */
    fun deleteKey()
}
