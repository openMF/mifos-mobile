/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore

import app.cash.turbine.test
import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import template.core.base.datastore.cache.LruCacheManager
import template.core.base.datastore.contracts.DataStoreChangeEvent
import template.core.base.datastore.handlers.BooleanTypeHandler
import template.core.base.datastore.handlers.DoubleTypeHandler
import template.core.base.datastore.handlers.FloatTypeHandler
import template.core.base.datastore.handlers.IntTypeHandler
import template.core.base.datastore.handlers.LongTypeHandler
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

@ExperimentalCoroutinesApi
class ReactiveUserPreferencesDataStoreTest {

    private val testDispatcher = StandardTestDispatcher()
    private val settings = MapSettings()
    private val changeNotifier = DefaultChangeNotifier()

    private val reactiveDataStore = ReactiveUserPreferencesDataStore(
        settings = settings,
        dispatcher = testDispatcher,
        typeHandlers = listOf(
            IntTypeHandler(),
            StringTypeHandler(),
            BooleanTypeHandler(),
            LongTypeHandler(),
            FloatTypeHandler(),
            DoubleTypeHandler(),
        ) as List<TypeHandler<Any>>,
        serializationStrategy = JsonSerializationStrategy(),
        validator = DefaultPreferencesValidator(),
        cacheManager = LruCacheManager(maxSize = 10),
        changeNotifier = changeNotifier,
        valueObserver = DefaultValueObserver(changeNotifier),
    )

    @Serializable
    data class TestUser(
        val id: Long,
        val name: String,
        val age: Int,
    )

    @Test
    fun observeValue_EmitsInitialValueAndUpdates() = runTest(testDispatcher) {
        // Initially store a value
        assertTrue(reactiveDataStore.putValue("test_key", "initial").isSuccess)

        reactiveDataStore.observeValue("test_key", "default").test {
            // Should emit initial value
            assertEquals("initial", awaitItem())

            // Update the value
            assertTrue(reactiveDataStore.putValue("test_key", "updated").isSuccess)
            assertEquals("updated", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun observeValue_EmitsDefaultWhenKeyNotExists() = runTest(testDispatcher) {
        reactiveDataStore.observeValue("non_existent", "default").test {
            assertEquals("default", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun observeSerializableValue_WorksWithCustomObjects() = runTest(testDispatcher) {
        val defaultUser = TestUser(0, "", 0)
        val testUser = TestUser(1, "John", 25)

        reactiveDataStore.observeSerializableValue(
            "user",
            defaultUser,
            TestUser.serializer(),
        ).test {
            // Should emit default initially
            assertEquals(defaultUser, awaitItem())

            // Update with new user
            assertTrue(
                reactiveDataStore.putSerializableValue(
                    "user",
                    testUser,
                    TestUser.serializer(),
                ).isSuccess,
            )
            assertEquals(testUser, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun observeChanges_EmitsCorrectChangeTypes() = runTest(testDispatcher) {
        reactiveDataStore.observeChanges().test {
            // Add a value
            assertTrue(reactiveDataStore.putValue("key1", "value1").isSuccess)
            val addChange = awaitItem()
            assertTrue(addChange is DataStoreChangeEvent.ValueAdded)
            assertEquals("key1", addChange.key)
            assertEquals("value1", addChange.value)

            // Update the value
            assertTrue(reactiveDataStore.putValue("key1", "value2").isSuccess)
            val updateChange = awaitItem()
            assertTrue(updateChange is DataStoreChangeEvent.ValueUpdated)
            assertEquals("key1", updateChange.key)
            assertEquals("value1", updateChange.oldValue)
            assertEquals("value2", updateChange.newValue)

            // Remove the value
            assertTrue(reactiveDataStore.removeValue("key1").isSuccess)
            val removeChange = awaitItem()
            assertTrue(removeChange is DataStoreChangeEvent.ValueRemoved)
            assertEquals("key1", removeChange.key)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun observeKeys_EmitsUpdatedKeySets() = runTest(context = testDispatcher) {
        reactiveDataStore.observeKeys().test {
            // Initial empty set (from onStart emission)
            assertEquals(emptySet(), awaitItem())
            advanceUntilIdle()

            // Add first key
            assertTrue(reactiveDataStore.putValue("key1", "value1").isSuccess)
            delay(300) // Allow time for initial emission

            assertEquals(setOf("key1"), awaitItem())

            // Add second key
            assertTrue(reactiveDataStore.putValue("key2", "value2").isSuccess)
            assertEquals(setOf("key1", "key2"), awaitItem())

            // Remove first key
            assertTrue(reactiveDataStore.removeValue("key1").isSuccess)
            assertEquals(setOf("key2"), awaitItem())

            // Remove second key
            assertTrue(reactiveDataStore.removeValue("key2").isSuccess)
            assertEquals(emptySet(), awaitItem())
        }
    }

    @Test
    fun observeSize_EmitsCorrectCounts() = runTest(testDispatcher) {
        reactiveDataStore.observeSize().test {
            // Initial size should be 0 (from onStart emission)
            assertEquals(0, awaitItem())

            // Add items
            assertTrue(reactiveDataStore.putValue("key1", "value1").isSuccess)
            assertEquals(1, awaitItem())

            assertTrue(reactiveDataStore.putValue("key2", "value2").isSuccess)
            assertEquals(2, awaitItem())

            // Clear all
            assertTrue(reactiveDataStore.clearAll().isSuccess)
            assertEquals(0, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun observeValue_DistinctUntilChanged() = runTest(testDispatcher) {
        reactiveDataStore.observeValue("key", "default").test {
            // Initial emission
            assertEquals("default", awaitItem())

            // Set same value - should not emit
            assertTrue(reactiveDataStore.putValue("key", "default").isSuccess)

            // Set different value - should emit
            assertTrue(reactiveDataStore.putValue("key", "new_value").isSuccess)
            assertEquals("new_value", awaitItem())

            // Set same value again - should not emit
            assertTrue(reactiveDataStore.putValue("key", "new_value").isSuccess)

            // Verify no more emissions
            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }
    }
}
