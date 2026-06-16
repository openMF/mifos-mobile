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
 * Unified authentication manager that coordinates failure tracking,
 * session lifecycle, and biometric authentication.
 *
 * Consumer apps inject this in their login/auth screen and call
 * [onAuthSuccess] or [onAuthFailure] — all security bookkeeping
 * is handled automatically.
 *
 * Usage:
 * ```kotlin
 * val authManager: SecureAuthManager = koinInject()
 * // on wrong password:
 * val action = authManager.onAuthFailure()
 * // on correct password:
 * authManager.onAuthSuccess()
 * ```
 */
class SecureAuthManager(
    private val failedAttemptTracker: FailedAttemptTracker,
    private val sessionManager: SessionManager,
    private val biometricAuthenticator: BiometricAuthenticator,
) {
    /**
     * Call after successful authentication. Resets the failure counter
     * and starts a new session.
     */
    fun onAuthSuccess() {
        failedAttemptTracker.reset()
        sessionManager.startSession()
    }

    /**
     * Call after failed authentication. Returns the action taken:
     * [FailureAction.ATTEMPT_RECORDED], [FailureAction.LOCKED_OUT],
     * or [FailureAction.DATA_WIPED].
     */
    fun onAuthFailure(): FailureAction =
        failedAttemptTracker.recordFailure()

    /** True if the user has exceeded the lockout threshold. */
    val isLockedOut: Boolean get() = failedAttemptTracker.isLockedOut

    /** Current number of consecutive failed attempts. */
    val failedCount: Int get() = failedAttemptTracker.currentFailedCount

    /**
     * Request biometric authentication. Returns the result
     * from the platform-specific biometric prompt.
     */
    suspend fun requestBiometric(
        reason: String = "Verify your identity",
    ): BiometricResult = biometricAuthenticator.authenticate(reason)
}
