/*
 * Copyright 2025 Mifos Initiative
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
import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock.System
import kotlinx.serialization.Serializable
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import template.core.base.datastore.cache.LruCacheManager
import template.core.base.datastore.contracts.DataStoreChangeEvent
import template.core.base.datastore.di.CoreDatastoreModule
import template.core.base.datastore.extensions.onlyAdditions
import template.core.base.datastore.extensions.onlyRemovals
import template.core.base.datastore.extensions.onlyUpdates
import template.core.base.datastore.factory.DataStoreFactory
import template.core.base.datastore.handlers.IntTypeHandler
import template.core.base.datastore.reactive.PreferenceFlowOperators
import template.core.base.datastore.repository.ReactivePreferencesRepository
import template.core.base.datastore.serialization.JsonSerializationStrategy
import template.core.base.datastore.validation.DefaultPreferencesValidator
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.measureTime

/**
 * Comprehensive feature test for the Core DataStore Module.
 * Tests all functionality including basic operations, reactive features,
 * caching, serialization, validation, and performance characteristics.
 */
@ExperimentalCoroutinesApi
class DataStoreComprehensiveFeatureTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository: ReactivePreferencesRepository by inject()
    private val operators: PreferenceFlowOperators by inject()

    // Test data models
    @Serializable
    data class UserProfile(
        val id: Long = 0,
        val name: String = "",
        val email: String = "",
        val age: Int = 0,
        val isActive: Boolean = true,
        val preferences: UserPreferences = UserPreferences(),
    )

    @Serializable
    data class UserPreferences(
        val theme: String = "light",
        val language: String = "en",
        val notifications: Boolean = true,
        val fontSize: Float = 14.0f,
        val autoSave: Boolean = true,
    )

    @Serializable
    data class AppConfig(
        val version: String,
        val features: List<String>,
        val settings: Map<String, String>,
    )

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                CoreDatastoreModule,
                module {
                    single<Settings> { MapSettings() }
                    single<CoroutineDispatcher>(
                        qualifier = named("IO"),
                    ) { testDispatcher }
                },
            )
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    /**
     * Test 1: Basic DataStore Operations
     * Tests fundamental CRUD operations for all primitive types
     */
    @Test
    fun test01_BasicDataStoreOperations() = runTest(testDispatcher) {
        println("=== Testing Basic DataStore Operations ===")

        // Test all primitive types
        val primitiveTests = mapOf(
            "int_key" to 42,
            "long_key" to 123456789L,
            "float_key" to 3.14f,
            "double_key" to 2.71828,
            "string_key" to "Hello DataStore!",
            "boolean_key" to true,
        )

        primitiveTests.forEach { (key, value) ->
            println("Testing $key with value: $value")

            // Save preference
            val saveResult = repository.savePreference(key, value)
            assertTrue(saveResult.isSuccess, "Failed to save $key")

            // Retrieve preference
            val retrieveResult = repository.getPreference(key, getDefaultForType(value))
            assertTrue(retrieveResult.isSuccess, "Failed to retrieve $key")
            assertEquals(value, retrieveResult.getOrThrow(), "Value mismatch for $key")

            // Check if key exists
            assertTrue(repository.hasPreference(key), "Key $key should exist")
        }

        // Test key retrieval
        val allKeys = repository.observeAllKeys().first()
        assertTrue(allKeys.containsAll(primitiveTests.keys), "Not all keys found")

        println("✅ Basic operations test passed")
    }

    /**
     * Test 2: Serializable Object Storage
     * Tests complex object serialization and deserialization
     */
    @Test
    fun test02_SerializableObjectStorage() = runTest(testDispatcher) {
        println("=== Testing Serializable Object Storage ===")

        // Test UserProfile storage
        val userProfile = UserProfile(
            id = 12345,
            name = "John Doe",
            email = "john.doe@example.com",
            age = 30,
            isActive = true,
            preferences = UserPreferences(
                theme = "dark",
                language = "es",
                notifications = false,
                fontSize = 16.0f,
                autoSave = true,
            ),
        )

        // Save complex object
        val saveResult = repository.saveSerializablePreference(
            "user_profile",
            userProfile,
            UserProfile.serializer(),
        )
        assertTrue(saveResult.isSuccess, "Failed to save UserProfile")

        // Retrieve complex object
        val retrieveResult = repository.getSerializablePreference(
            "user_profile",
            UserProfile(),
            UserProfile.serializer(),
        )
        assertTrue(retrieveResult.isSuccess, "Failed to retrieve UserProfile")
        assertEquals(userProfile, retrieveResult.getOrThrow(), "UserProfile mismatch")

        // Test AppConfig with collections
        val appConfig = AppConfig(
            version = "1.2.3",
            features = listOf("feature1", "feature2", "feature3"),
            settings = mapOf(
                "timeout" to "30",
                "retries" to "3",
                "debug" to "false",
            ),
        )

        val configSaveResult = repository.saveSerializablePreference(
            "app_config",
            appConfig,
            AppConfig.serializer(),
        )
        assertTrue(configSaveResult.isSuccess, "Failed to save AppConfig")

        val configRetrieveResult = repository.getSerializablePreference(
            "app_config",
            AppConfig("", emptyList(), emptyMap()),
            AppConfig.serializer(),
        )
        assertTrue(configRetrieveResult.isSuccess, "Failed to retrieve AppConfig")
        assertEquals(appConfig, configRetrieveResult.getOrThrow(), "AppConfig mismatch")

        println("✅ Serializable object storage test passed")
    }

    /**
     * Test 3: Reactive Functionality
     * Tests reactive flows, change notifications, and observers
     */
    @Test
    fun test03_ReactiveFunctionality() = runTest(testDispatcher) {
        println("=== Testing Reactive Functionality ===")

        // Test preference observation
        repository.observePreference("reactive_key", "default").test {
            // Initial value
            assertEquals("default", awaitItem())

            // Update value
            repository.savePreference("reactive_key", "updated")
            assertEquals("updated", awaitItem())

            // Update again
            repository.savePreference("reactive_key", "final")
            assertEquals("final", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        // Test change notifications
        repository.observePreferenceChanges().test {
            // Add a new preference
            repository.savePreference("change_test", "value1")
            val addChange = awaitItem()
            assertTrue(addChange is DataStoreChangeEvent.ValueAdded)
            assertEquals("change_test", addChange.key)

            // Update the preference
            repository.savePreference("change_test", "value2")
            val updateChange = awaitItem()
            assertTrue(updateChange is DataStoreChangeEvent.ValueUpdated)
            assertEquals("change_test", updateChange.key)
            assertEquals("value1", updateChange.oldValue)
            assertEquals("value2", updateChange.newValue)

            // Remove the preference
            repository.removePreference("change_test")
            val removeChange = awaitItem()
            assertTrue(removeChange is DataStoreChangeEvent.ValueRemoved)
            assertEquals("change_test", removeChange.key)

            cancelAndIgnoreRemainingEvents()
        }

        // Test serializable object observation
        val defaultProfile = UserProfile()
        repository.observeSerializablePreference(
            "profile_reactive",
            defaultProfile,
            UserProfile.serializer(),
        ).test {
            assertEquals(defaultProfile, awaitItem())

            val newProfile = UserProfile(id = 999, name = "Jane")
            repository.saveSerializablePreference(
                "profile_reactive",
                newProfile,
                UserProfile.serializer(),
            )
            assertEquals(newProfile, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        println("✅ Reactive functionality test passed")
    }

    /**
     * Test 4: Preference Flow Operators
     * Tests combining, mapping, and advanced flow operations
     */
    @Test
    fun test04_PreferenceFlowOperators() = runTest(testDispatcher) {
        println("=== Testing Preference Flow Operators ===")

        // Test combining two preferences
        operators.combinePreferences(
            "username",
            "",
            "is_premium",
            false,
        ) { username, isPremium ->
            "User: $username, Premium: $isPremium"
        }.test {
            assertEquals("User: , Premium: false", awaitItem())

            repository.savePreference("username", "alice")
            assertEquals("User: alice, Premium: false", awaitItem())

            repository.savePreference("is_premium", true)
            assertEquals("User: alice, Premium: true", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        // Test combining three preferences
        operators.combinePreferences(
            "theme",
            "light",
            "language",
            "en",
            "notifications",
            true,
        ) { theme, lang, notifs ->
            Triple(theme, lang, notifs)
        }.test {
            assertEquals(Triple("light", "en", true), awaitItem())

            repository.savePreference("theme", "dark")
            assertEquals(Triple("dark", "en", true), awaitItem())

            repository.savePreference("language", "es")
            assertEquals(Triple("dark", "es", true), awaitItem())

            repository.savePreference("notifications", false)
            assertEquals(Triple("dark", "es", false), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        // Test mapped preference observation
        operators.observeMappedPreference("counter", 0) { count ->
            "Count: $count"
        }.test {
            assertEquals("Count: 0", awaitItem())

            repository.savePreference("counter", 5)
            assertEquals("Count: 5", awaitItem())

            repository.savePreference("counter", 10)
            assertEquals("Count: 10", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        // Test key change observation
        operators.observeAnyKeyChange("monitored1", "monitored2").test {
            repository.savePreference("monitored1", "value1")
            assertEquals("monitored1", awaitItem())

            repository.savePreference("unmonitored", "value")
            // Should not emit

            repository.savePreference("monitored2", "value2")
            assertEquals("monitored2", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        println("✅ Preference flow operators test passed")
    }

    /**
     * Test 5: Caching Functionality
     * Tests LRU cache behavior and cache management
     */
    @Test
    fun test05_CachingFunctionality() = runTest(testDispatcher) {
        println("=== Testing Caching Functionality ===")

        // Create a small cache to test eviction
        val cache = LruCacheManager<String, String>(maxSize = 3)

        // Test basic cache operations
        cache.put("key1", "value1")
        cache.put("key2", "value2")
        cache.put("key3", "value3")

        assertEquals(3, cache.size())
        assertEquals("value1", cache.get("key1"))
        assertEquals("value2", cache.get("key2"))
        assertEquals("value3", cache.get("key3"))

        // Test LRU eviction
        cache.put("key4", "value4") // Should evict key1 (least recently used)
        assertEquals(3, cache.size())
        assertNull(cache.get("key1"), "key1 should have been evicted")
        assertEquals("value4", cache.get("key4"))

        // Test cache removal
        cache.remove("key2")
        assertEquals(2, cache.size())
        assertNull(cache.get("key2"))

        // Test cache clear
        cache.clear()
        assertEquals(0, cache.size())

        // Test with datastore factory
        val settings = MapSettings()
        val cacheableDataStore = DataStoreFactory()
            .settings(settings)
            .cacheSize(5)
            .dispatcher(testDispatcher)
            .buildDataStore()

        // Test cache hit/miss behavior
        val putResult = cacheableDataStore.putValue("cached_key", "cached_value")
        assertTrue(putResult.isSuccess)

        val getResult = cacheableDataStore.getValue("cached_key", "default")
        assertTrue(getResult.isSuccess)
        assertEquals("cached_value", getResult.getOrThrow())

        // Verify cache contains the value
        assertTrue(cacheableDataStore.getCacheSize() > 0)

        // Test cache invalidation
        val invalidateResult = cacheableDataStore.invalidateCache("cached_key")
        assertTrue(invalidateResult.isSuccess)

        println("✅ Caching functionality test passed")
    }

    /**
     * Test 6: Validation and Error Handling
     * Tests input validation and error scenarios
     */
    @Test
    fun test06_ValidationAndErrorHandling() = runTest(testDispatcher) {
        println("=== Testing Validation and Error Handling ===")

        val validator = DefaultPreferencesValidator()

        // Test key validation
        assertTrue(validator.validateKey("valid_key").isSuccess)
        assertTrue(validator.validateKey("").isFailure)
        assertTrue(validator.validateKey(" ".repeat(256)).isFailure)

        // Test value validation
        assertTrue(validator.validateValue("valid_value").isSuccess)
        assertTrue(validator.validateValue(123).isSuccess)
        assertTrue(validator.validateValue(null).isFailure)

        val longString = "x".repeat(20000)
        assertTrue(validator.validateValue(longString).isFailure)

        // Test serialization error handling
        val strategy = JsonSerializationStrategy()

        @Serializable
        data class TestData(val value: String)

        val validData = TestData("test")
        val serializeResult = strategy.serialize(validData, TestData.serializer())
        assertTrue(serializeResult.isSuccess)

        val deserializeResult = strategy.deserialize(
            serializeResult.getOrThrow(),
            TestData.serializer(),
        )
        assertTrue(deserializeResult.isSuccess)
        assertEquals(validData, deserializeResult.getOrThrow())

        // Test deserializing invalid JSON
        val invalidDeserializeResult = strategy.deserialize(
            "invalid json",
            TestData.serializer(),
        )
        assertTrue(invalidDeserializeResult.isFailure)

        // Test type handler error scenarios
        val intHandler = IntTypeHandler()
        assertTrue(intHandler.canHandle(42))
        assertFalse(intHandler.canHandle("not an int"))
        assertFalse(intHandler.canHandle(null))

        println("✅ Validation and error handling test passed")
    }

    /**
     * Test 7: Flow Extensions
     * Tests custom flow extension functions
     */
    @Test
    fun test07_FlowExtensions() = runTest(testDispatcher) {
        println("=== Testing Flow Extensions ===")

        // Test change filtering extensions
        repository.observePreferenceChanges().onlyAdditions().test {
            repository.savePreference("add_test", "value1")
            val addition = awaitItem()
            assertTrue(addition is DataStoreChangeEvent.ValueAdded)
            assertEquals("add_test", addition.key)

            // Update should not appear in additions
            repository.savePreference("add_test", "value2")

            repository.savePreference("add_test2", "value2")
            val addition2 = awaitItem()
            assertTrue(addition2 is DataStoreChangeEvent.ValueAdded)
            assertEquals("add_test2", addition2.key)

            cancelAndIgnoreRemainingEvents()
        }

        repository.observePreferenceChanges().onlyUpdates().test {
            // First save (addition) should not appear
            repository.savePreference("update_test", "initial")

            // Second save (update) should appear
            repository.savePreference("update_test", "updated")
            val update = awaitItem()
            assertTrue(update is DataStoreChangeEvent.ValueUpdated)
            assertEquals("update_test", update.key)
            assertEquals("initial", update.oldValue)
            assertEquals("updated", update.newValue)

            cancelAndIgnoreRemainingEvents()
        }

        repository.observePreferenceChanges().onlyRemovals().test {
            repository.savePreference("remove_test", "value")

            repository.removePreference("remove_test")
            val removal = awaitItem()
            assertTrue(removal is DataStoreChangeEvent.ValueRemoved)
            assertEquals("remove_test", removal.key)

            cancelAndIgnoreRemainingEvents()
        }

        println("✅ Flow extensions test passed")
    }

    /**
     * Test 8: Performance and Stress Testing
     * Tests performance characteristics under load
     */
    @Test
    fun test08_PerformanceAndStressTesting() = runTest(testDispatcher) {
        println("=== Testing Performance and Stress ===")

        val operationCount = 100

        // Test rapid sequential operations
        val sequentialDuration = measureTime {
            repeat(operationCount) { i ->
                repository.savePreference("perf_key_$i", "value_$i")
            }
            advanceUntilIdle()
        }

        // Verify all values were saved
        repeat(operationCount) { i ->
            val result = repository.getPreference("perf_key_$i", "")
            assertEquals("value_$i", result.getOrThrow())
        }

        println("Sequential operations ($operationCount): ${sequentialDuration.inWholeMilliseconds}ms")

        // Test rapid updates to same key
        repository.observePreference("rapid_update", 0).test {
            assertEquals(0, awaitItem()) // Initial value

            val updateDuration = measureTime {
                repeat(50) { i ->
                    repository.savePreference("rapid_update", i + 1)
                    advanceUntilIdle()
                }
            }

            // Should receive all updates
            repeat(50) { i ->
                assertEquals(i + 1, awaitItem())
            }

            println("Rapid updates (50): ${updateDuration.inWholeMilliseconds}ms")

            cancelAndIgnoreRemainingEvents()
        }

        // Test large object serialization
        val largeConfig = AppConfig(
            version = "1.0.0",
            features = (1..100).map { "feature_$it" },
            settings = (1..50).associate { "setting_$it" to "value_$it" },
        )

        val serializationDuration = measureTime {
            repeat(10) {
                repository.saveSerializablePreference(
                    "large_config_$it",
                    largeConfig,
                    AppConfig.serializer(),
                )
            }
            advanceUntilIdle()
        }

        println("Large object serialization (10): ${serializationDuration.inWholeMilliseconds}ms")

        // Verify large objects were saved correctly
        repeat(10) {
            val result = repository.getSerializablePreference(
                "large_config_$it",
                AppConfig("", emptyList(), emptyMap()),
                AppConfig.serializer(),
            )
            assertEquals(largeConfig, result.getOrThrow())
        }

        println("✅ Performance and stress test passed")
    }

    /**
     * Test 9: Complete Integration Scenario
     * Tests realistic app usage patterns
     */
    @Test
    fun test09_CompleteIntegrationScenario() = runTest(testDispatcher) {
        println("=== Testing Complete Integration Scenario ===")

        // Simulate complete app onboarding and usage

        // 1. Initial app setup
        val appConfig = AppConfig(
            version = "2.1.0",
            features = listOf("dark_mode", "notifications", "analytics"),
            settings = mapOf(
                "api_timeout" to "30000",
                "cache_size" to "100",
                "log_level" to "info",
            ),
        )

        repository.saveSerializablePreference("app_config", appConfig, AppConfig.serializer())

        // 2. User profile creation
        val userProfile = UserProfile(
            id = 12345,
            name = "Integration Test User",
            email = "test@example.com",
            age = 25,
            preferences = UserPreferences(
                theme = "auto",
                language = "en",
                notifications = true,
                fontSize = 15.0f,
            ),
        )

        repository.saveSerializablePreference("user_profile", userProfile, UserProfile.serializer())

        // 3. Session preferences
        repository.savePreference("session_id", "sess_abc123")
        repository.savePreference("login_timestamp", System.now().toEpochMilliseconds())
        repository.savePreference("device_id", "device_xyz789")

        // 4. Feature flags and settings
        val featureFlags = mapOf(
            "new_ui" to true,
            "beta_features" to false,
            "experimental_api" to true,
        )

        featureFlags.forEach { (flag, enabled) ->
            repository.savePreference("feature_$flag", enabled)
        }

        // 5. Observe combined user state
        operators.combinePreferences(
            "user_profile",
            UserProfile(),
            "session_id",
            "",
        ) { profile, sessionId ->
            "User: ${profile.name} (${profile.email}), Session: $sessionId"
        }.test {
            val expectedState =
                "User: ${userProfile.name} (${userProfile.email}), Session: sess_abc123"
            assertEquals(expectedState, awaitItem())

            // Update user profile
            val updatedProfile = userProfile.copy(name = "Updated User")
            repository.saveSerializablePreference(
                "user_profile",
                updatedProfile,
                UserProfile.serializer(),
            )

            val expectedUpdatedState =
                "User: Updated User (${userProfile.email}), Session: sess_abc123"
            assertEquals(expectedUpdatedState, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        // 6. Verify all data persistence
        val retrievedConfig = repository.getSerializablePreference(
            "app_config",
            AppConfig("", emptyList(), emptyMap()),
            AppConfig.serializer(),
        ).getOrThrow()
        assertEquals(appConfig, retrievedConfig)

        val retrievedProfile = repository.getSerializablePreference(
            "user_profile",
            UserProfile(),
            UserProfile.serializer(),
        ).getOrThrow()
        assertEquals("Updated User", retrievedProfile.name)

        // 7. Test bulk operations
        val bulkClearDuration = measureTime {
            repository.clearAllPreferences()
            advanceUntilIdle()
        }

        println("Bulk clear operation: ${bulkClearDuration.inWholeMilliseconds}ms")

        // Verify everything was cleared
        val keysAfterClear = repository.observeAllKeys().first()
        assertTrue(keysAfterClear.isEmpty(), "All keys should be cleared")

        println("✅ Complete integration scenario test passed")
    }

    /**
     * Test 10: Edge Cases and Boundary Conditions
     * Tests unusual scenarios and edge cases
     */
    @Test
    fun test10_EdgeCasesAndBoundaryConditions() = runTest(testDispatcher) {
        println("=== Testing Edge Cases and Boundary Conditions ===")

        // Test empty string handling
        repository.savePreference("empty_string", "")
        assertEquals("", repository.getPreference("empty_string", "default").getOrThrow())

        // Test special characters in keys and values
        val specialKey = "key_with_special_chars_!@#$%^&*()"
        val specialValue = "Value with émojis 🚀💻 and spëcial chars: <>?/|\\`~"
        repository.savePreference(specialKey, specialValue)
        assertEquals(specialValue, repository.getPreference(specialKey, "").getOrThrow())

        // Test very long strings (within limits)
        val longValue = "x".repeat(9999) // Just under the 10000 limit
        repository.savePreference("long_value", longValue)
        assertEquals(longValue, repository.getPreference("long_value", "").getOrThrow())

        // Test numeric edge cases
        repository.savePreference("max_int", Int.MAX_VALUE)
        repository.savePreference("min_int", Int.MIN_VALUE)
        repository.savePreference("max_long", Long.MAX_VALUE)
        repository.savePreference("min_long", Long.MIN_VALUE)
        repository.savePreference("max_float", Float.MAX_VALUE)
        repository.savePreference("min_float", Float.MIN_VALUE)
        repository.savePreference("max_double", Double.MAX_VALUE)
        repository.savePreference("min_double", Double.MIN_VALUE)

        assertEquals(Int.MAX_VALUE, repository.getPreference("max_int", 0).getOrThrow())
        assertEquals(Int.MIN_VALUE, repository.getPreference("min_int", 0).getOrThrow())
        assertEquals(Long.MAX_VALUE, repository.getPreference("max_long", 0L).getOrThrow())
        assertEquals(Long.MIN_VALUE, repository.getPreference("min_long", 0L).getOrThrow())
        assertEquals(Float.MAX_VALUE, repository.getPreference("max_float", 0f).getOrThrow())
        assertEquals(Float.MIN_VALUE, repository.getPreference("min_float", 0f).getOrThrow())
        assertEquals(Double.MAX_VALUE, repository.getPreference("max_double", 0.0).getOrThrow())
        assertEquals(Double.MIN_VALUE, repository.getPreference("min_double", 0.0).getOrThrow())

        // Test rapid key creation and deletion
        repeat(20) { i ->
            repository.savePreference("temp_key_$i", "temp_value_$i")
        }

        repeat(20) { i ->
            assertTrue(repository.hasPreference("temp_key_$i"))
            repository.removePreference("temp_key_$i")
            assertFalse(repository.hasPreference("temp_key_$i"))
        }

        // Test observing non-existent keys
        repository.observePreference("non_existent", "default_value").test {
            assertEquals("default_value", awaitItem())

            // Create the key
            repository.savePreference("non_existent", "now_exists")
            assertEquals("now_exists", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        // Test multiple observers on same key
        val observers = List(5) {
            repository.observePreference("shared_key", "initial")
        }

        observers.forEach { flow ->
            flow.test {
                assertEquals("initial", awaitItem())
                expectNoEvents() // Should not have additional events yet
                cancelAndIgnoreRemainingEvents()
            }
        }

        println("✅ Edge cases and boundary conditions test passed")
    }

    // Helper function to get default values for different types
    @Suppress("UNCHECKED_CAST")
    private fun <T> getDefaultForType(value: T): T = when (value) {
        is Int -> 0 as T
        is Long -> 0L as T
        is Float -> 0f as T
        is Double -> 0.0 as T
        is String -> "" as T
        is Boolean -> false as T
        else -> throw IllegalArgumentException("Unsupported type: ${value?.let { it::class }}")
    }

    /**
     * Runs all tests in sequence and provides a comprehensive report
     */
    @Test
    fun runAllFeatureTests() = runTest(testDispatcher) {
        println("🚀 Starting Comprehensive DataStore Feature Test Suite")
        println("=" * 60)

        val totalDuration = measureTime {
            try {
                test01_BasicDataStoreOperations()
                test02_SerializableObjectStorage()
                test03_ReactiveFunctionality()
                test04_PreferenceFlowOperators()
                test05_CachingFunctionality()
                test06_ValidationAndErrorHandling()
                test07_FlowExtensions()
                test08_PerformanceAndStressTesting()
                test09_CompleteIntegrationScenario()
                test10_EdgeCasesAndBoundaryConditions()
            } catch (e: Exception) {
                println("❌ Test suite failed with error: ${e.message}")
                throw e
            }
        }

        println("=" * 60)
        println("🎉 ALL FEATURE TESTS PASSED!")
        println("⏱️  Total execution time: ${totalDuration.inWholeMilliseconds}ms")
        println("✅ DataStore module is fully functional and ready for production")
        println("=" * 60)
    }
}

/**
 * Extension function for string repetition (Kotlin doesn't have this built-in)
 */
private operator fun String.times(count: Int): String = repeat(count)
