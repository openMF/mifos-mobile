/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.testing.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest as kotlinxRunTest

/**
 * Test coroutine utilities and extensions.
 *
 * These utilities make it easier to test ViewModels and other
 * coroutine-based code.
 */

/**
 * Interface for providing test dispatchers to ViewModels.
 */
interface TestDispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}

/**
 * Implementation of dispatcher provider using test dispatchers.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class StandardTestDispatcherProvider(
    testDispatcher: TestDispatcher = StandardTestDispatcher(),
) : TestDispatcherProvider {
    override val main: CoroutineDispatcher = testDispatcher
    override val io: CoroutineDispatcher = testDispatcher
    override val default: CoroutineDispatcher = testDispatcher
}

/**
 * Runs a test with automatic dispatcher setup.
 *
 * Usage:
 * ```kotlin
 * @Test
 * fun `my test`() = runTestWithDispatcher {
 *     // Test code with test dispatcher configured
 *     advanceUntilIdle()
 * }
 * ```
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun runTestWithDispatcher(
    testBody: suspend TestScope.() -> Unit,
) = kotlinxRunTest {
    testBody()
}

/**
 * Extension to advance time by a specific duration.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun TestScope.advanceBy(milliseconds: Long) {
    advanceTimeBy(milliseconds)
}

/**
 * Extension to advance until all coroutines are idle.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun TestScope.advanceToIdle() {
    advanceUntilIdle()
}
