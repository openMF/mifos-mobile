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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Flow testing utilities for common test scenarios.
 *
 * Usage:
 * ```kotlin
 * @Test
 * fun `test flow emissions`() = runTest {
 *     val viewModel = MyViewModel()
 *
 *     viewModel.stateFlow.assertEmits(
 *         MyState(loading = true),
 *         MyState(loading = false, data = "result")
 *     )
 * }
 * ```
 */

/**
 * Collects all emissions from the flow and asserts they match the expected values.
 */
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Flow<T>.assertEmitsInOrder(
    testScope: TestScope,
    vararg expected: T,
) {
    val emissions = mutableListOf<T>()
    val job = testScope.launch(UnconfinedTestDispatcher(testScope.testScheduler)) {
        toList(emissions)
    }

    testScope.advanceUntilIdle()
    job.cancel()

    assertEquals(
        expected.toList(),
        emissions.take(expected.size),
        "Flow emissions did not match expected order",
    )
}

/**
 * Asserts that the flow emits at least one value matching the predicate.
 */
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Flow<T>.assertContainsEmission(
    testScope: TestScope,
    predicate: (T) -> Boolean,
) {
    val emissions = mutableListOf<T>()
    val job = testScope.launch(UnconfinedTestDispatcher(testScope.testScheduler)) {
        toList(emissions)
    }

    testScope.advanceUntilIdle()
    job.cancel()

    assertTrue(
        emissions.any(predicate),
        "Flow did not emit any value matching the predicate",
    )
}

/**
 * Gets the first emission from the flow.
 */
suspend fun <T> Flow<T>.firstEmission(): T = first()

/**
 * Collects emissions until the predicate is satisfied.
 */
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Flow<T>.collectUntil(
    testScope: TestScope,
    predicate: (T) -> Boolean,
): List<T> {
    val emissions = mutableListOf<T>()
    val job = testScope.launch(UnconfinedTestDispatcher(testScope.testScheduler)) {
        collect { value ->
            emissions.add(value)
            if (predicate(value)) {
                return@collect
            }
        }
    }

    testScope.advanceUntilIdle()
    job.cancel()

    return emissions
}
