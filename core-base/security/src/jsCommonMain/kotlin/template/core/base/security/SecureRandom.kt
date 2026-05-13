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

/** Fail-closed until WebCrypto (`crypto.getRandomValues`) integration is available. */
actual class SecureRandom {
    actual fun nextBytes(size: Int): ByteArray {
        throw SecurityException("Secure random generation is unavailable on JS/Wasm until WebCrypto is integrated")
    }
}
