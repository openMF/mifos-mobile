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

import co.touchlab.kermit.Logger
import java.io.File
import java.util.Arrays

actual class SecureWiper actual constructor() {

    actual fun wipeSecureStorage() {
        Logger.w("SecureWiper") { "Secure storage wipe triggered" }
        val secureDir = File(System.getProperty("user.home"), ".mifos-secure")
        if (secureDir.exists()) {
            secureDir.listFiles()?.forEach { file ->
                // Overwrite before delete
                val length = file.length()
                file.writeBytes(ByteArray(length.toInt()))
                file.delete()
            }
        }
    }

    actual fun scrubMemory(data: ByteArray) {
        Arrays.fill(data, 0.toByte())
    }
}
