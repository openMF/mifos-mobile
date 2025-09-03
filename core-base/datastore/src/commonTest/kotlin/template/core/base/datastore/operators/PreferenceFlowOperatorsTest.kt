/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.operators

import app.cash.turbine.test
import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import template.core.base.datastore.cache.LruCacheManager
import template.core.base.datastore.handlers.BooleanTypeHandler
import template.core.base.datastore.handlers.IntTypeHandler
import template.core.base.datastore.handlers.StringTypeHandler
import template.core.base.datastore.handlers.TypeHandler
import template.core.base.datastore.reactive.DefaultChangeNotifier
import template.core.base.datastore.reactive.DefaultValueObserver
import template.core.base.datastore.reactive.PreferenceFlowOperators
import template.core.base.datastore.repository.DefaultReactivePreferencesRepository
import template.core.base.datastore.serialization.JsonSerializationStrategy
import template.core.base.datastore.store.ReactiveUserPreferencesDataStore
import template.core.base.datastore.validation.DefaultPreferencesValidator
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test suite for [template.core.base.datastore.reactive.PreferenceFlowOperators].
 * Verifies combining, mapping, and observing preference flows, including edge cases.
 */
@ExperimentalCoroutinesApi
class PreferenceFlowOperatorsTest {

    private val testDispatcher = StandardTestDispatcher()
    private val changeNotifier = DefaultChangeNotifier()

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
        cacheManager = LruCacheManager(),
        changeNotifier = changeNotifier,
        valueObserver = DefaultValueObserver(changeNotifier),
    )

    private val repository = DefaultReactivePreferencesRepository(reactiveDataStore)
    private val operators = PreferenceFlowOperators(repository)

    /**
     * Tests combining two preference flows and verifies correct emission order.
     */
    @Test
    fun combinePreferences_TwoValues_CombinesCorrectly() = runTest(testDispatcher) {
        operators.combinePreferences(
            "key1",
            "default1",
            "key2",
            "default2",
        ) { value1, value2 ->
            "$value1-$value2"
        }.test {
            // Initial combined value
            assertEquals("default1-default2", awaitItem())

            // Update first preference
            repository.savePreference("key1", "new1")
            assertEquals("new1-default2", awaitItem())

            // Update second preference
            repository.savePreference("key2", "new2")
            assertEquals("new1-new2", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Tests combining three preference flows and verifies correct emission order.
     */
    @Test
    fun combinePreferences_ThreeValues_CombinesCorrectly() = runTest(testDispatcher) {
        operators.combinePreferences(
            "theme",
            "light",
            "language",
            "en",
            "notifications",
            true,
        ) { theme, language, notifications ->
            Triple(theme, language, notifications)
        }.test {
            advanceUntilIdle()
            delay(10)

            // Initial combined value
            assertEquals(Triple("light", "en", true), awaitItem())

            // Give time for initial emission
            kotlinx.coroutines.delay(10)

            // Update theme
            repository.savePreference("theme", "dark")
            advanceUntilIdle()
            assertEquals(Triple("dark", "en", true), awaitItem())

            // Update notifications
            repository.savePreference("notifications", false)
            delay(10)
            advanceUntilIdle()

            assertEquals(Triple("dark", "en", false), awaitItem())
        }
    }

    /**
     * Tests observing changes to any of the specified keys.
     */
    @Test
    fun observeAnyKeyChange_EmitsOnSpecifiedKeys() = runTest(testDispatcher) {
        operators.observeAnyKeyChange("key1", "key2").test {
            // Change to key1 - should emit
            repository.savePreference("key1", "value1")
            assertEquals("key1", awaitItem())

            // Change to key3 - should not emit (not in watched keys)
            repository.savePreference("key3", "value3")

            // Change to key2 - should emit
            repository.savePreference("key2", "value2")
            assertEquals("key2", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Tests mapping a preference value using a transform function.
     */
    @Test
    fun observeMappedPreference_TransformsValues() = runTest(testDispatcher) {
        operators.observeMappedPreference("count", 0) { count ->
            "Count is: $count"
        }.test {
            // Initial mapped value
            assertEquals("Count is: 0", awaitItem())

            // Update preference
            repository.savePreference("count", 5)
            assertEquals("Count is: 5", awaitItem())

            // Update again
            repository.savePreference("count", 10)
            assertEquals("Count is: 10", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Tests rapid updates to preferences and ensures all changes are emitted.
     */
    @Test
    fun rapidUpdates_AreHandledCorrectly() = runTest(testDispatcher) {
        operators.observeMappedPreference("rapid", 0) { it }.test {
            for (i in 1..5) {
                repository.savePreference("rapid", i)
                assertEquals(i, awaitItem())
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Tests combining preferences with default/null values.
     */
    @Test
    fun combinePreferences_DefaultValues() = runTest(testDispatcher) {
        operators.combinePreferences(
            "missing1",
            "",
            "missing2",
            "",
        ) { v1, v2 ->
            v1 to v2
        }.test {
            assertEquals("" to "", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
