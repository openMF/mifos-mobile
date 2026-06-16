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

actual class FieldEncryptor(private val _keyProvider: SecureKeyProvider) {

    actual fun encrypt(plaintext: String): String {
        throw SecurityException(
            "Native field encryption is unavailable until AES-GCM is implemented for this target",
        )
    }

    actual fun decrypt(ciphertext: String): String {
        throw SecurityException(
            "Native field decryption is unavailable until AES-GCM is implemented for this target",
        )
    }

    actual fun encrypt(data: ByteArray): ByteArray {
        throw SecurityException(
            "Native field encryption is unavailable until AES-GCM is implemented for this target",
        )
    }

    actual fun decrypt(data: ByteArray): ByteArray {
        throw SecurityException(
            "Native field decryption is unavailable until AES-GCM is implemented for this target",
        )
    }
}
