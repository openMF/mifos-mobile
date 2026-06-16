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

/**
 * Tracks failed authentication attempts and triggers lockout or data wipe
 * when configured thresholds are exceeded.
 *
 * @param policy Security policy with attempt thresholds.
 * @param secureWiper Used to wipe data when wipe threshold is reached.
 * @param onLockout Callback invoked when lock threshold is reached.
 */
class FailedAttemptTracker(
    private val policy: SecurityPolicy,
    private val secureWiper: SecureWiper,
    private val onLockout: () -> Unit = {},
) {
    @kotlin.concurrent.Volatile
    private var failedCount: Int = 0

    val currentFailedCount: Int get() = failedCount

    val isLockedOut: Boolean get() = failedCount >= policy.lockAfterFailedAttempts

    /**
     * Record a failed attempt. Returns the action taken.
     */
    fun recordFailure(): FailureAction {
        failedCount++
        Logger.d("FailedAttemptTracker") { "Failed attempt $failedCount/${policy.wipeAfterFailedAttempts}" }

        return when {
            failedCount >= policy.wipeAfterFailedAttempts -> {
                Logger.w("FailedAttemptTracker") { "Wipe threshold reached — wiping secure storage" }
                secureWiper.wipeSecureStorage()
                failedCount = 0
                FailureAction.DATA_WIPED
            }
            failedCount >= policy.lockAfterFailedAttempts -> {
                onLockout()
                FailureAction.LOCKED_OUT
            }
            else -> FailureAction.ATTEMPT_RECORDED
        }
    }

    /** Reset the counter after a successful authentication. */
    fun reset() {
        failedCount = 0
    }
}

enum class FailureAction {
    ATTEMPT_RECORDED,
    LOCKED_OUT,
    DATA_WIPED,
}
