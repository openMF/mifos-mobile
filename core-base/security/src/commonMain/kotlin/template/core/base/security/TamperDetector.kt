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
 * Detects runtime environment tampering such as root/jailbreak, debugger
 * attachment, and signature mismatch.
 *
 * Each platform provides its own detection heuristics via `expect/actual`.
 * Results are advisory — consumer apps decide what action to take (warn,
 * restrict, or wipe).
 */
expect class TamperDetector() {

    /** Returns true if the device appears rooted or jailbroken. */
    fun isDeviceCompromised(): Boolean

    /** Returns true if a debugger is currently attached. */
    fun isDebuggerAttached(): Boolean

    /** Returns true if the app signature matches the expected release signature. */
    fun isSignatureValid(): Boolean
}
