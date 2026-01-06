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
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * Native-specific test utilities.
 *
 * This file provides Kotlin/Native specific testing helpers.
 * Works for iOS, macOS, Linux, Windows native targets.
 */
object NativeTestUtils {

    /**
     * Sets up the Main dispatcher for Native testing.
     * Call this in @BeforeTest.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun setupMainDispatcher(testDispatcher: TestDispatcher = StandardTestDispatcher()) {
        Dispatchers.setMain(testDispatcher)
    }

    /**
     * Resets the Main dispatcher after Native testing.
     * Call this in @AfterTest.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun resetMainDispatcher() {
        Dispatchers.resetMain()
    }

    /**
     * Creates a time mark for measuring elapsed time.
     * Use with [elapsedSince] to measure durations.
     */
    fun markNow(): kotlin.time.TimeMark = kotlin.time.TimeSource.Monotonic.markNow()

    /**
     * Gets the elapsed time in milliseconds since the given mark.
     */
    fun elapsedMillisSince(mark: kotlin.time.TimeMark): Long = mark.elapsedNow().inWholeMilliseconds
}
