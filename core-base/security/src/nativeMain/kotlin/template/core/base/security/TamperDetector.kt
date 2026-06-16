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

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.Foundation.NSFileManager
import platform.posix.getenv

@OptIn(ExperimentalForeignApi::class)
actual class TamperDetector actual constructor() {

    actual fun isDeviceCompromised(): Boolean {
        return checkJailbreakIndicators()
    }

    actual fun isDebuggerAttached(): Boolean {
        // sysctl-based debugger detection is possible but platform-specific.
        // Consumer apps can override with a more robust check.
        return false
    }

    actual fun isSignatureValid(): Boolean = true

    private fun checkJailbreakIndicators(): Boolean {
        val jailbreakPaths = listOf(
            "/Applications/Cydia.app",
            "/usr/sbin/sshd",
            "/bin/bash",
            "/usr/bin/ssh",
            "/private/var/lib/apt/",
        )
        val fileManager = NSFileManager.defaultManager
        if (jailbreakPaths.any { fileManager.fileExistsAtPath(it) }) return true

        // Check for DYLD_INSERT_LIBRARIES
        return getenv("DYLD_INSERT_LIBRARIES") != null
    }
}
