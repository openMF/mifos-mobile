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
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * JUnit4 TestRule for Android that sets the Main dispatcher to a test dispatcher.
 *
 * Usage:
 * ```kotlin
 * class MyViewModelTest {
 *     @get:Rule
 *     val mainDispatcherRule = MainDispatcherTestRule()
 *
 *     @Test
 *     fun testSomething() = runTest {
 *         // Main dispatcher is automatically set to test dispatcher
 *     }
 * }
 * ```
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherTestRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher(),
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
