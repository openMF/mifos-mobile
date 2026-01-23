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

import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import template.core.base.datastore.cache.LruCacheManager
import template.core.base.datastore.handlers.BooleanTypeHandler
import template.core.base.datastore.handlers.IntTypeHandler
import template.core.base.datastore.handlers.StringTypeHandler
import template.core.base.datastore.handlers.TypeHandler
import template.core.base.datastore.serialization.JsonSerializationStrategy
import template.core.base.datastore.store.CachedPreferencesStore
import template.core.base.datastore.validation.DefaultPreferencesValidator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class EnhancedUserPreferencesDataStoreTest {
    private val testDispatcher = StandardTestDispatcher()
    private val settings = MapSettings()
    private val cacheManager = LruCacheManager<String, Any>(maxSize = 2)

    @Suppress("UNCHECKED_CAST")
    private val dataStore = CachedPreferencesStore(
        settings = settings,
        dispatcher = testDispatcher,
        typeHandlers = listOf(
            IntTypeHandler(),
            StringTypeHandler(),
            BooleanTypeHandler(),
        ) as List<TypeHandler<Any>>,
        serializationStrategy = JsonSerializationStrategy(),
        validator = DefaultPreferencesValidator(),
        cacheManager = cacheManager,
    )

    @Serializable
    data class Custom(val id: Int, val name: String)

    @Test
    fun putAndGet_PrimitiveTypes() = runTest(testDispatcher) {
        assertTrue(dataStore.putValue("int", 1).isSuccess)
        assertEquals(1, dataStore.getValue("int", 0).getOrThrow())
        assertTrue(dataStore.putValue("bool", true).isSuccess)
        assertEquals(true, dataStore.getValue("bool", false).getOrThrow())
    }

    @Test
    fun putAndGet_SerializableType() = runTest(testDispatcher) {
        val custom = Custom(1, "abc")
        assertTrue(dataStore.putSerializableValue("custom", custom, Custom.serializer()).isSuccess)
        assertEquals(custom, dataStore.getSerializableValue("custom", Custom(0, ""), Custom.serializer()).getOrThrow())
    }

    @Test
    fun getValue_ReturnsDefaultIfMissingOrCorrupt() = runTest(testDispatcher) {
        assertEquals(99, dataStore.getValue("missing", 99).getOrThrow())
    }

    @Test
    fun hasKey_WorksWithCache() = runTest(testDispatcher) {
        assertTrue(dataStore.hasKey("nope").getOrThrow() == false)
        assertTrue(dataStore.putValue("exists", 1).isSuccess)
        assertTrue(dataStore.hasKey("exists").getOrThrow())
    }

    @Test
    fun removeValue_RemovesFromCacheAndSettings() = runTest(testDispatcher) {
        assertTrue(dataStore.putValue("toremove", 5).isSuccess)
        assertTrue(dataStore.removeValue("toremove").isSuccess)
        assertEquals(0, dataStore.getValue("toremove", 0).getOrThrow())
    }

    @Test
    fun clearAll_RemovesEverything() = runTest(testDispatcher) {
        assertTrue(dataStore.putValue("a", 1).isSuccess)
        assertTrue(dataStore.putValue("b", 2).isSuccess)
        assertTrue(dataStore.clearAll().isSuccess)
        assertEquals(0, dataStore.getValue("a", 0).getOrThrow())
        assertEquals(0, dataStore.getValue("b", 0).getOrThrow())
    }

    @Test
    fun getAllKeysAndSize() = runTest(testDispatcher) {
        assertTrue(dataStore.putValue("k1", 1).isSuccess)
        assertTrue(dataStore.putValue("k2", 2).isSuccess)
        assertEquals(setOf("k1", "k2"), dataStore.getAllKeys().getOrThrow())
        assertEquals(2, dataStore.getSize().getOrThrow())
    }

    @Test
    fun cacheEviction_WorksAsExpected() = runTest(testDispatcher) {
        assertTrue(dataStore.putValue("k1", 1).isSuccess)
        assertTrue(dataStore.putValue("k2", 2).isSuccess)
        assertTrue(dataStore.putValue("k3", 3).isSuccess) // Should evict k1 if maxSize=2
        assertEquals(2, dataStore.getCacheSize())
        assertTrue(!cacheManager.containsKey("k1"))
    }

    @Test
    fun invalidateCache_RemovesSpecificKey() = runTest(testDispatcher) {
        assertTrue(dataStore.putValue("k1", 1).isSuccess)
        assertTrue(dataStore.invalidateCache("k1").isSuccess)
        assertTrue(!cacheManager.containsKey("k1"))
    }

    @Test
    fun invalidateAllCache_RemovesAll() = runTest(testDispatcher) {
        assertTrue(dataStore.putValue("k1", 1).isSuccess)
        assertTrue(dataStore.putValue("k2", 2).isSuccess)
        assertTrue(dataStore.invalidateAllCache().isSuccess)
        assertEquals(0, dataStore.getCacheSize())
    }

    @Test
    fun putValue_FailsForUnsupportedType() = runTest(testDispatcher) {
        class Unsupported
        val result = dataStore.putValue("bad", Unsupported())
        assertTrue(result.isFailure)
    }
}
