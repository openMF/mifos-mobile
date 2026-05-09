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
 * Platform-specific AES-256-GCM field encryption for sensitive data.
 *
 * Encrypts individual fields BEFORE they are stored in Room or Settings.
 * Keys are managed by [SecureKeyProvider] using hardware-backed storage
 * where available (Android Keystore, iOS Keychain, etc.).
 */
expect class FieldEncryptor {
    /**
     * Encrypts plaintext to a Base64-encoded ciphertext string.
     * The returned value is prefixed with "ENC:" for format detection.
     */
    fun encrypt(plaintext: String): String

    /** Decrypts a Base64-encoded ciphertext string back to plaintext. */
    fun decrypt(ciphertext: String): String

    /** Encrypts raw bytes. */
    fun encrypt(data: ByteArray): ByteArray

    /** Decrypts raw bytes. */
    fun decrypt(data: ByteArray): ByteArray
}
