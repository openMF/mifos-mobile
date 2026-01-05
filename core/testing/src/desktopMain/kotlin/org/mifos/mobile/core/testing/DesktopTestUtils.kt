/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.testing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * Desktop-specific test utilities.
 *
 * This file provides Desktop (JVM) specific testing helpers.
 * Desktop uses Swing dispatcher for UI operations.
 */
object DesktopTestUtils {

    /**
     * Sets up dispatchers for Desktop UI testing.
     * Call this in @BeforeTest for tests that involve Compose Desktop UI.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun setupTestDispatchers(testDispatcher: TestDispatcher = StandardTestDispatcher()) {
        Dispatchers.setMain(testDispatcher)
    }

    /**
     * Resets dispatchers after Desktop UI testing.
     * Call this in @AfterTest.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun resetTestDispatchers() {
        Dispatchers.resetMain()
    }

    /**
     * Returns the Swing dispatcher for Desktop UI operations.
     * Useful for tests that need to interact with Swing event dispatch thread.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun swingDispatcher() = Dispatchers.Swing
}
