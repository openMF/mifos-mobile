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

actual class SecureWiper actual constructor() {

    actual fun wipeSecureStorage() {
        Logger.w("SecureWiper") { "Secure storage wipe triggered" }
        // Clear localStorage/sessionStorage in browser context.
    }

    actual fun scrubMemory(data: ByteArray) {
        for (i in data.indices) {
            data[i] = 0
        }
    }
}
