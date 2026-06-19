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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages user session lifecycle with inactivity timeout.
 *
 * Call [touch] on every user interaction to reset the inactivity timer.
 * Call [checkTimeout] periodically (e.g., on app foreground) to verify
 * the session hasn't expired.
 *
 * @param policy Security policy with session timeout configuration.
 * @param onSessionExpired Callback invoked when session expires.
 * @param clock Time source for testability; defaults to system clock.
 */
class SessionManager(
    private val policy: SecurityPolicy,
    private val onSessionExpired: () -> Unit = {},
    private val clock: () -> Long = { currentTimeMillis() },
) {
    private val _isSessionActive = MutableStateFlow(false)
    val isSessionActive: StateFlow<Boolean> = _isSessionActive.asStateFlow()

    @kotlin.concurrent.Volatile
    private var lastActivityTime: Long = clock()

    /** Start a new session. */
    fun startSession() {
        lastActivityTime = clock()
        _isSessionActive.value = true
        Logger.d("SessionManager") { "Session started" }
    }

    /** Record user activity to reset the inactivity timer. */
    fun touch() {
        if (_isSessionActive.value) {
            lastActivityTime = clock()
        }
    }

    /**
     * Check if the session has timed out due to inactivity.
     * If expired, invokes [onSessionExpired] and returns true.
     */
    fun checkTimeout(): Boolean {
        if (!_isSessionActive.value) return false

        val elapsed = clock() - lastActivityTime
        val timeoutMs = policy.sessionTimeoutMinutes * 60 * 1_000L

        return if (elapsed >= timeoutMs) {
            Logger.d("SessionManager") { "Session expired after ${policy.sessionTimeoutMinutes}min" }
            endSession()
            onSessionExpired()
            true
        } else {
            false
        }
    }

    /** End the current session. */
    fun endSession() {
        _isSessionActive.value = false
        Logger.d("SessionManager") { "Session ended" }
    }
}

internal fun currentTimeMillis(): Long =
    kotlin.time.Clock.System.now().toEpochMilliseconds()
