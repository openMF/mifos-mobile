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

class SecurityStateTest {

    @Test
    fun defaultStateIsNotCompromised() {
        val state = SecurityState()
        assertFalse(state.isCompromised)
    }

    @Test
    fun defaultStateIsNotSessionActive() {
        val state = SecurityState()
        assertFalse(state.isSessionActive)
    }

    @Test
    fun isLockedIsTrueWhenSessionInactive() {
        val state = SecurityState()
        state.isSessionActive = false
        assertTrue(state.isLocked)
    }

    @Test
    fun isLockedIsFalseWhenSessionActive() {
        val state = SecurityState()
        state.isSessionActive = true
        assertFalse(state.isLocked)
    }
}
