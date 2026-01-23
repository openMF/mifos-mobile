/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.performance

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import template.core.base.datastore.cache.LruCacheManager
import template.core.base.datastore.handlers.BooleanTypeHandler
import template.core.base.datastore.handlers.IntTypeHandler
import template.core.base.datastore.handlers.StringTypeHandler
import template.core.base.datastore.handlers.TypeHandler
import template.core.base.datastore.reactive.DefaultChangeNotifier
import template.core.base.datastore.reactive.DefaultValueObserver
import template.core.base.datastore.serialization.JsonSerializationStrategy
import template.core.base.datastore.store.ReactiveUserPreferencesDataStore
import template.core.base.datastore.validation.DefaultPreferencesValidator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.measureTime

@ExperimentalCoroutinesApi
class ReactivePerformanceTest {

    private val testDispatcher = StandardTestDispatcher()
    private val changeNotifier = DefaultChangeNotifier()

    @Suppress("UNCHECKED_CAST")
    private val reactiveDataStore = ReactiveUserPreferencesDataStore(
        settings = MapSettings(),
        dispatcher = testDispatcher,
        typeHandlers = listOf(
            IntTypeHandler(),
            StringTypeHandler(),
            BooleanTypeHandler(),
        ) as List<TypeHandler<Any>>,
        serializationStrategy = JsonSerializationStrategy(),
        validator = DefaultPreferencesValidator(),
        cacheManager = LruCacheManager(maxSize = 1000),
        changeNotifier = changeNotifier,
        valueObserver = DefaultValueObserver(changeNotifier),
    )

    @Test
    fun rapidUpdates_HandledEfficiently() = runTest(testDispatcher) {
        val updateCount = 100

        reactiveDataStore.observeValue("counter", 0).test {
            // Initial value
            assertEquals(0, awaitItem())

            // Give time for initial emission
            delay(10)

            val duration = measureTime {
                repeat(updateCount) { i ->
                    reactiveDataStore.putValue("counter", i + 1)
                    advanceUntilIdle()
                }
                yield()
                advanceUntilIdle()
            }

            // Should receive all updates in order
            val received = mutableListOf<Int>()
            repeat(updateCount) {
                received.add(awaitItem())
            }
            assertEquals((1..updateCount).toList(), received)

            // Verify performance is reasonable (this is a rough check)
            assertTrue(
                duration.inWholeMilliseconds < 5000,
                "Updates took too long: \\${duration.inWholeMilliseconds}ms",
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun multipleObservers_ShareNotifications() = runTest(testDispatcher) {
        turbineScope {
            val observer1 = reactiveDataStore
                .observeValue("shared_key", "default").testIn(backgroundScope)

            val observer2 =
                reactiveDataStore.observeValue("shared_key", "default").testIn(backgroundScope)

            advanceUntilIdle()

            // Both should get initial value
            assertEquals("default", observer1.awaitItem())
            assertEquals("default", observer2.awaitItem())

            // Give time for initial emission
            delay(10)

            // Update the value
            reactiveDataStore.putValue("shared_key", "updated")

            // Both should get updated value
            assertEquals("updated", observer1.awaitItem())
            assertEquals("updated", observer2.awaitItem())
        }
    }
}
