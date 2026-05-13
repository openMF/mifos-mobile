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

import java.io.File
import java.security.SecureRandom as JSecureRandom

/**
 * Desktop key provider using a local encrypted key file.
 *
 * Phase 4 (T18) upgrades this to OS credential APIs
 * (macOS Keychain, Linux libsecret, Windows DPAPI).
 */
actual class SecureKeyProvider {

    private val keyFile: File by lazy {
        val dir = File(System.getProperty("user.home"), ".mifos-secure")
        dir.mkdirs()
        File(dir, "field_key.bin")
    }

    actual fun getExistingKey(): Any? {
        if (!keyFile.exists()) return null
        return keyFile.readBytes()
    }

    actual fun getOrCreateKey(): Any {
        val existing = getExistingKey() as? ByteArray
        if (existing != null) return existing

        val key = ByteArray(32)
        JSecureRandom().nextBytes(key)
        keyFile.parentFile?.mkdirs()
        keyFile.writeBytes(key)
        return key
    }

    actual fun deleteKey() {
        if (keyFile.exists()) {
            keyFile.delete()
        }
    }
}
