/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class SessionManager {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var lastInteractionTime = 0L
    private var isMonitoring = false
    private val timeoutMs = Constants.TIMEOUT_SESSION_MS

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    @OptIn(ExperimentalTime::class)
    fun startSession() {
        if (isMonitoring) return
        isMonitoring = true
        lastInteractionTime = Clock.System.now().toEpochMilliseconds()
        startHeartbeat()
    }

    @OptIn(ExperimentalTime::class)
    fun userInteracted() {
        if (!isMonitoring) return
        lastInteractionTime = Clock.System.now().toEpochMilliseconds()
    }

    fun stopSession() {
        isMonitoring = false
    }

    @OptIn(ExperimentalTime::class)
    private fun startHeartbeat() {
        scope.launch {
            while (isMonitoring) {
                val currentTime = Clock.System.now().toEpochMilliseconds()
                if (currentTime - lastInteractionTime >= timeoutMs) {
                    withContext(Dispatchers.Main) {
                        _logoutEvent.emit(Unit)
                    }
                    stopSession()
                    break
                }
                delay(30_000)
            }
        }
    }
}
