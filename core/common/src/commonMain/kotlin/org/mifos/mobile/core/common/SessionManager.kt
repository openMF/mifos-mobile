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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

interface SessionStorage {
    suspend fun saveSessionTime(time: Long)
    fun getSessionTime(): Flow<Long>
}

class SessionManager(
    private val sessionStorage: SessionStorage,
) {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val checkLock = Mutex()

    private var heartbeatJob: Job? = null

    @OptIn(ExperimentalAtomicApi::class)
    private val lastInteractionTime = AtomicLong(0L)

    @OptIn(ExperimentalAtomicApi::class)
    private val lastDiskSaveTime = AtomicLong(0L)

    @OptIn(ExperimentalAtomicApi::class)
    private val isMonitoring = AtomicBoolean(false)
    private val timeoutMs = Constants.TIMEOUT_SESSION_MS

    private val _isExpired = MutableStateFlow(false)
    val isExpired = _isExpired.asStateFlow()

    @OptIn(ExperimentalAtomicApi::class)
    private val isColdStart = AtomicBoolean(true)

    private val _shouldShowDialog = MutableStateFlow(false)
    val shouldShowDialog = _shouldShowDialog.asStateFlow()

    @OptIn(ExperimentalTime::class, ExperimentalAtomicApi::class)
    fun startSession() {
        if (isMonitoring.compareAndSet(expectedValue = false, newValue = true)) {
            _isExpired.value = false
            scope.launch {
                val savedTime = sessionStorage.getSessionTime().first()
                val now = Clock.System.now().toEpochMilliseconds()

                val effectiveTime = if (savedTime == 0L) now else savedTime

                lastInteractionTime.store(effectiveTime)
                checkExpirationInternal()

                if (!_isExpired.value) {
                    heartbeatJob = startHeartbeat()
                }
            }
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalAtomicApi::class)
    fun userInteracted() {
        if (_isExpired.value) return
        if (isMonitoring.load()) {
            isColdStart.store(false)

            val now = Clock.System.now().toEpochMilliseconds()
            lastInteractionTime.store(now)

            val lastSave = lastDiskSaveTime.load()
            if (now - lastSave > Constants.THROTTLE_DISK_SAVE_MS) {
                lastDiskSaveTime.store(now)
                scope.launch {
                    sessionStorage.saveSessionTime(now)
                }
            }
        }
    }

    @OptIn(ExperimentalAtomicApi::class)
    fun stopSession() {
        isMonitoring.store(false)
        _isExpired.value = false
        _shouldShowDialog.value = false
        heartbeatJob?.cancel()
        heartbeatJob = null
        isColdStart.store(true)
        lastInteractionTime.store(0L)

        scope.launch {
            sessionStorage.saveSessionTime(0L)
        }
    }

    fun checkExpirationNow() {
        scope.launch {
            checkExpirationInternal()
        }
    }

    @OptIn(ExperimentalAtomicApi::class, ExperimentalTime::class)
    private suspend fun checkExpirationInternal() {
        checkLock.withLock {
            if (_isExpired.value) return

            val ramTime = lastInteractionTime.load()

            val effectiveTime = if (ramTime == 0L) {
                sessionStorage.getSessionTime().first()
            } else {
                ramTime
            }

            if (effectiveTime == 0L) return

            val currentTime = Clock.System.now().toEpochMilliseconds()

            if (currentTime - effectiveTime >= timeoutMs) {
                if (isColdStart.compareAndSet(expectedValue = true, newValue = false)) {
                    _shouldShowDialog.value = false
                } else {
                    _shouldShowDialog.value = true
                }
                _isExpired.value = true
                isMonitoring.store(true)
            } else {
                isColdStart.store(false)
            }
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalAtomicApi::class)
    private fun startHeartbeat(): Job {
        return scope.launch {
            while (isMonitoring.load()) {
                checkExpirationInternal()
                delay(30_000)
            }
        }
    }
}
