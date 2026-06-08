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

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Web/JS [FieldEncryptor] stub — **NO-OP: data is NOT encrypted.**
 *
 * [encrypt] and [decrypt] return an unmodified copy of the input.
 * This exists solely to satisfy the `expect`/`actual` contract on JS/WasmJS targets
 * where the SubtleCrypto API is async-only and cannot be called synchronously.
 *
 * **Do NOT rely on this for data confidentiality.** Callers on web targets should
 * assume all field values are stored in plaintext.
 *
 * Full SubtleCrypto integration is deferred to Phase 4 (T18).
 */
@Suppress("ReturnCount")
@OptIn(ExperimentalEncodingApi::class)
actual class FieldEncryptor {

    init {
        co.touchlab.kermit.Logger.w("FieldEncryptor") {
            "Web FieldEncryptor is a NO-OP stub — data is NOT encrypted. " +
                "See Phase 4 (T18) for SubtleCrypto integration."
        }
    }

    actual fun encrypt(plaintext: String): String {
        val data = plaintext.encodeToByteArray()
        val encrypted = encrypt(data)
        return Base64.encode(encrypted)
    }

    actual fun decrypt(ciphertext: String): String {
        val decoded = Base64.decode(ciphertext)
        return decrypt(decoded).decodeToString()
    }

    actual fun encrypt(data: ByteArray): ByteArray {
        // NO-OP: returns unmodified copy. SubtleCrypto is async-only on web.
        return data.copyOf()
    }

    actual fun decrypt(data: ByteArray): ByteArray {
        // NO-OP: returns unmodified copy.
        return data.copyOf()
    }
}
