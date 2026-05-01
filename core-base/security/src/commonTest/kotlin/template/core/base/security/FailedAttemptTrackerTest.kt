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

class FailedAttemptTrackerTest {

    private val policy = SecurityPolicy(
        lockAfterFailedAttempts = 3,
        wipeAfterFailedAttempts = 5,
    )

    private fun createTracker(
        onLockout: () -> Unit = {},
    ): FailedAttemptTracker {
        return FailedAttemptTracker(policy, SecureWiper(), onLockout)
    }

    @Test
    fun initialStateIsNotLockedOut() {
        val tracker = createTracker()
        assertFalse(tracker.isLockedOut)
        assertEquals(0, tracker.currentFailedCount)
    }

    @Test
    fun recordFailureIncrementsCount() {
        val tracker = createTracker()
        val result = tracker.recordFailure()
        assertEquals(FailureAction.ATTEMPT_RECORDED, result)
        assertEquals(1, tracker.currentFailedCount)
    }

    @Test
    fun lockoutAfterThreshold() {
        var lockedOut = false
        val tracker = createTracker { lockedOut = true }
        repeat(2) { tracker.recordFailure() }
        val result = tracker.recordFailure()
        assertEquals(FailureAction.LOCKED_OUT, result)
        assertTrue(tracker.isLockedOut)
        assertTrue(lockedOut)
    }

    @Test
    fun wipeAfterMaxThreshold() {
        val tracker = createTracker()
        repeat(4) { tracker.recordFailure() }
        val result = tracker.recordFailure()
        assertEquals(FailureAction.DATA_WIPED, result)
        assertEquals(0, tracker.currentFailedCount)
    }

    @Test
    fun resetClearsCount() {
        val tracker = createTracker()
        repeat(2) { tracker.recordFailure() }
        tracker.reset()
        assertEquals(0, tracker.currentFailedCount)
        assertFalse(tracker.isLockedOut)
    }
}
