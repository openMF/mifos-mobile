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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SecureAuthManagerTest {

    private val policy = SecurityPolicy(
        lockAfterFailedAttempts = 3,
        wipeAfterFailedAttempts = 5,
    )

    private fun createManager(): SecureAuthManager {
        val wiper = SecureWiper()
        val tracker = FailedAttemptTracker(policy, wiper)
        val session = SessionManager(policy)
        val biometric = BiometricAuthenticator()
        return SecureAuthManager(tracker, session, biometric)
    }

    @Test
    fun onAuthSuccessResetsTrackerAndStartsSession() {
        val manager = createManager()
        manager.onAuthFailure()
        manager.onAuthSuccess()
        assertEquals(0, manager.failedCount)
    }

    @Test
    fun onAuthFailureReturnsAttemptRecordedForFirstFailure() {
        val manager = createManager()
        val result = manager.onAuthFailure()
        assertEquals(FailureAction.ATTEMPT_RECORDED, result)
    }

    @Test
    fun onAuthFailureReturnsLockedOutAtThreshold() {
        val manager = createManager()
        repeat(2) { manager.onAuthFailure() }
        val result = manager.onAuthFailure()
        assertEquals(FailureAction.LOCKED_OUT, result)
    }

    @Test
    fun onAuthFailureReturnsDataWipedAtWipeThreshold() {
        val manager = createManager()
        repeat(4) { manager.onAuthFailure() }
        val result = manager.onAuthFailure()
        assertEquals(FailureAction.DATA_WIPED, result)
    }

    @Test
    fun isLockedOutReflectsTrackerState() {
        val manager = createManager()
        assertFalse(manager.isLockedOut)
        repeat(3) { manager.onAuthFailure() }
        assertTrue(manager.isLockedOut)
    }

    @Test
    fun onAuthSuccessAfterLockoutResetsCountAndClearsLockout() {
        val manager = createManager()
        repeat(3) { manager.onAuthFailure() }
        assertTrue(manager.isLockedOut)
        manager.onAuthSuccess()
        assertEquals(0, manager.failedCount)
        assertFalse(manager.isLockedOut)
    }
}
