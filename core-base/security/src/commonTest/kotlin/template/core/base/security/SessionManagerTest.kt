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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SessionManagerTest {

    private val policy = SecurityPolicy(sessionTimeoutMinutes = 30)

    @Test
    fun initialStateIsInactive() {
        val manager = SessionManager(policy)
        assertFalse(manager.isSessionActive.value)
    }

    @Test
    fun startSessionActivates() {
        val manager = SessionManager(policy)
        manager.startSession()
        assertTrue(manager.isSessionActive.value)
    }

    @Test
    fun endSessionDeactivates() {
        val manager = SessionManager(policy)
        manager.startSession()
        manager.endSession()
        assertFalse(manager.isSessionActive.value)
    }

    @Test
    fun checkTimeoutReturnsFalseWhenInactive() {
        val manager = SessionManager(policy)
        assertFalse(manager.checkTimeout())
    }

    @Test
    fun checkTimeoutReturnsFalseWhenFreshlyStarted() {
        val manager = SessionManager(policy)
        manager.startSession()
        assertFalse(manager.checkTimeout())
    }
}
