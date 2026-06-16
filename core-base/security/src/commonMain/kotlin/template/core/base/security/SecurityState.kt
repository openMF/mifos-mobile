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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Observable security state exposed to the UI layer via [LocalSecurityState].
 *
 * Updated automatically by [SecurityGate]. Consumer composables read this
 * to react to security events (lock screen, tamper warning, etc.) without
 * injecting individual security components.
 */
class SecurityState {
    /** True if the device appears rooted/jailbroken or a debugger is attached. */
    var isCompromised: Boolean by mutableStateOf(false)

    /** True when the user has an active, non-expired session. */
    var isSessionActive: Boolean by mutableStateOf(false)

    /** Convenience: true when there is no active session. */
    val isLocked: Boolean get() = !isSessionActive
}

/**
 * CompositionLocal providing [SecurityState] to the composable tree.
 *
 * Provided by [SecurityGate]. Throws if accessed outside of a SecurityGate.
 */
val LocalSecurityState = staticCompositionLocalOf<SecurityState> {
    error("No SecurityState provided - wrap your app with SecurityGate")
}
