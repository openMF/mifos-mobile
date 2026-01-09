/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.testing.rule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * Main dispatcher rule for coroutine testing.
 *
 * Usage:
 * ```kotlin
 * class MyViewModelTest {
 *     private val testDispatcher = StandardTestDispatcher()
 *     private val dispatcherRule = MainDispatcherRule(testDispatcher)
 *
 *     @BeforeTest
 *     fun setup() {
 *         dispatcherRule.before()
 *     }
 *
 *     @AfterTest
 *     fun tearDown() {
 *         dispatcherRule.after()
 *     }
 * }
 * ```
 *
 * For Android JUnit4, see the Android-specific implementation that extends TestWatcher.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher(),
) {
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    fun after() {
        Dispatchers.resetMain()
    }
}
