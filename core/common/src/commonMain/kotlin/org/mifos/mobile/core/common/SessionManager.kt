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
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class SessionManager {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private var heartbeatJob: Job? = null
    @OptIn(ExperimentalAtomicApi::class)
    private val lastInteractionTime = AtomicLong(0L)

    @OptIn(ExperimentalAtomicApi::class)
    private val isMonitoring = AtomicBoolean(false)
    private val timeoutMs = Constants.TIMEOUT_SESSION_MS

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    @OptIn(ExperimentalTime::class, ExperimentalAtomicApi::class)
    fun startSession() {
        if (isMonitoring.compareAndSet(expectedValue = false, newValue = true)) {
            lastInteractionTime.store(Clock.System.now().toEpochMilliseconds())
            heartbeatJob = startHeartbeat()
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalAtomicApi::class)
    fun userInteracted() {
        if (isMonitoring.load()) {
            lastInteractionTime.store(Clock.System.now().toEpochMilliseconds())
        }
    }

    @OptIn(ExperimentalAtomicApi::class)
    fun stopSession() {
        isMonitoring.store(false)
        heartbeatJob?.cancel()
        heartbeatJob = null
    }

    @OptIn(ExperimentalTime::class, ExperimentalAtomicApi::class)
    private fun startHeartbeat(): Job {
        return scope.launch {
            while (isMonitoring.load()) {
                val currentTime = Clock.System.now().toEpochMilliseconds()
                if (currentTime - lastInteractionTime.load() >= timeoutMs) {
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
