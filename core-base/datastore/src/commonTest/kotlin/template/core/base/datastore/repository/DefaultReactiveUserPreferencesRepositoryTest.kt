/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.repository

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

@ExperimentalCoroutinesApi
class DefaultReactiveUserPreferencesRepositoryTest {

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

    @Serializable
    data class AppSettings(
        val theme: String,
        val language: String,
        val notifications: Boolean,
    )

    @Test
    fun observePreference_ReactsToChanges() = runTest(testDispatcher) {
        repository.observePreference("theme", "light").test {
            // Initial value
            assertEquals("light", awaitItem())

            // Save new preference
            repository.savePreference("theme", "dark")
            assertEquals("dark", awaitItem())

            // Save another value
            repository.savePreference("theme", "auto")
            assertEquals("auto", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun observeSerializablePreference_ReactsToComplexObjects() = runTest(testDispatcher) {
        val defaultSettings = AppSettings("light", "en", true)

        repository.observeSerializablePreference(
            "app_settings",
            defaultSettings,
            AppSettings.serializer(),
        ).test {
            // Initial default
            assertEquals(defaultSettings, awaitItem())

            // Update settings
            val newSettings = AppSettings("dark", "es", false)
            repository.saveSerializablePreference(
                "app_settings",
                newSettings,
                AppSettings.serializer(),
            )
            assertEquals(newSettings, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun observePreferenceChanges_FiltersCorrectly() = runTest(testDispatcher) {
        repository.observePreferenceChanges("specific_key").test {
            // Save to different key - should not emit
            repository.savePreference("other_key", "value")

            // Save to specific key - should emit
            repository.savePreference("specific_key", "value")
            val change = awaitItem()
            assertTrue(change is DataStoreChangeEvent.ValueAdded)
            assertEquals("specific_key", change.key)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun observeAllKeys_ReactsToKeyChanges() = runTest(testDispatcher) {
        repository.observeAllKeys().test {
            // Initial empty set (from onStart emission)
            assertEquals(emptySet(), awaitItem())
            advanceUntilIdle()
            delay(100) // Ensure we wait for any initial emissions

            // Add preferences
            repository.savePreference("key1", "value1")
            assertEquals(setOf("key1"), awaitItem())

            repository.savePreference("key2", "value2")
            assertEquals(setOf("key1", "key2"), awaitItem())

            // Remove preference
            repository.removePreference("key1")
            assertEquals(setOf("key2"), awaitItem())

            // Clear all
            repository.clearAllPreferences()
            assertEquals(emptySet(), awaitItem())
        }
    }

    @Test
    fun observePreferenceCount_ReactsToSizeChanges() = runTest(testDispatcher) {
        repository.observePreferenceCount().test {
            // Initial count (from onStart emission)
            assertEquals(0, awaitItem())

            // Add preferences
            repository.savePreference("key1", "value1")
            assertEquals(1, awaitItem())

            repository.savePreference("key2", "value2")
            assertEquals(2, awaitItem())

            // Clear all
            repository.clearAllPreferences()
            assertEquals(0, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}
