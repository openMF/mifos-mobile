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
import java.util.Arrays

actual class SecureWiper actual constructor() {

    actual fun wipeSecureStorage() {
        Logger.w("SecureWiper") { "Secure storage wipe triggered" }
        // Consumer apps should clear EncryptedSharedPreferences,
        // delete encryption keys from Android Keystore, and
        // clear Room database here.
    }

    actual fun scrubMemory(data: ByteArray) {
        Arrays.fill(data, 0.toByte())
    }
}
