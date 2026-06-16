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
 * Web/JS [FieldEncryptor] stub — fail-closed until SubtleCrypto integration.
 *
 * The synchronous API surface cannot safely call async-only SubtleCrypto.
 * To avoid insecure fallback behavior, encryption/decryption operations throw.
 *
 * Full SubtleCrypto integration is deferred to Phase 4 (T18).
 */
actual class FieldEncryptor {

    init {
        co.touchlab.kermit.Logger.w("FieldEncryptor") {
            "Web FieldEncryptor is fail-closed until SubtleCrypto integration."
        }
    }

    actual fun encrypt(plaintext: String): String {
        throw SecurityException("Field encryption is unavailable on JS/Wasm until SubtleCrypto is integrated")
    }

    actual fun decrypt(ciphertext: String): String {
        throw SecurityException("Field decryption is unavailable on JS/Wasm until SubtleCrypto is integrated")
    }

    actual fun encrypt(data: ByteArray): ByteArray {
        throw SecurityException("Field encryption is unavailable on JS/Wasm until SubtleCrypto is integrated")
    }

    actual fun decrypt(data: ByteArray): ByteArray {
        throw SecurityException("Field decryption is unavailable on JS/Wasm until SubtleCrypto is integrated")
    }
}
