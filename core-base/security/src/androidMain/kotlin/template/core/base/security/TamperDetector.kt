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

import android.os.Build
import android.os.Debug
import java.io.File

actual class TamperDetector actual constructor() {

    actual fun isDeviceCompromised(): Boolean {
        return checkRootIndicators()
    }

    actual fun isDebuggerAttached(): Boolean {
        return Debug.isDebuggerConnected() || Debug.waitingForDebugger()
    }

    actual fun isSignatureValid(): Boolean {
        // Consumer apps should override with their release signature hash
        return true
    }

    @Suppress("SwallowedException")
    private fun checkRootIndicators(): Boolean {
        val rootPaths = listOf(
            "/system/app/Superuser.apk",
            "/system/xbin/su",
            "/system/bin/su",
            "/sbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
        )
        val hasRootFiles = rootPaths.any { File(it).exists() }
        val hasTestKeys = Build.TAGS?.contains("test-keys") == true
        val canExecSu = try {
            Runtime.getRuntime().exec("su")
            true
        } catch (_: Exception) {
            false
        }
        return hasRootFiles || hasTestKeys || canExecSu
    }
}
