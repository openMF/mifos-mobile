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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    private val _isExpired = MutableStateFlow(false)
    val isExpired = _isExpired.asStateFlow()

    @OptIn(ExperimentalTime::class, ExperimentalAtomicApi::class)
    fun startSession() {
        if (isMonitoring.compareAndSet(expectedValue = false, newValue = true)) {
            _isExpired.value = false
            lastInteractionTime.store(Clock.System.now().toEpochMilliseconds())
            heartbeatJob = startHeartbeat()
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalAtomicApi::class)
    fun userInteracted() {
        if (_isExpired.value) return
        if (isMonitoring.load()) {
            lastInteractionTime.store(Clock.System.now().toEpochMilliseconds())
        }
    }

    @OptIn(ExperimentalAtomicApi::class)
    fun stopSession() {
        isMonitoring.store(false)
        _isExpired.value = false
        heartbeatJob?.cancel()
        heartbeatJob = null
    }

    @OptIn(ExperimentalTime::class, ExperimentalAtomicApi::class)
    private fun startHeartbeat(): Job {
        return scope.launch {
            while (isMonitoring.load()) {
                val currentTime = Clock.System.now().toEpochMilliseconds()
                if (currentTime - lastInteractionTime.load() >= timeoutMs) {
                    _isExpired.value = true
                    break
                }
                delay(30_000)
            }
        }
    }
}
