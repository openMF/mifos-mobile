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

import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import template.core.base.datastore.store.BasicPreferencesStore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class UserPreferencesDataStoreTest {
    private val testDispatcher = StandardTestDispatcher()
    private val settings = MapSettings()
    private val dataStore = BasicPreferencesStore(settings, testDispatcher)

    @Serializable
    data class CustomData(val id: Int, val name: String)

    @Test
    fun putAndGet_PrimitiveTypes() = runTest(testDispatcher) {
        assertTrue(dataStore.putValue("int", 42).isSuccess)
        assertEquals(42, dataStore.getValue("int", 0).getOrThrow())

        assertTrue(dataStore.putValue("long", 123L).isSuccess)
        assertEquals(123L, dataStore.getValue("long", 0L).getOrThrow())

        assertTrue(dataStore.putValue("float", 3.14f).isSuccess)
        assertEquals(3.14f, dataStore.getValue("float", 0f).getOrThrow())

        assertTrue(dataStore.putValue("double", 2.71).isSuccess)
        assertEquals(2.71, dataStore.getValue("double", 0.0).getOrThrow())

        assertTrue(dataStore.putValue("string", "hello").isSuccess)
        assertEquals("hello", dataStore.getValue("string", "").getOrThrow())

        assertTrue(dataStore.putValue("boolean", true).isSuccess)
        assertEquals(true, dataStore.getValue("boolean", false).getOrThrow())
    }

    @Test
    fun putAndGet_CustomType_WithSerializer() = runTest(testDispatcher) {
        val custom = CustomData(1, "test")
        assertTrue(dataStore.putValue("custom", custom, CustomData.serializer()).isSuccess)
        assertEquals(custom, dataStore.getValue("custom", CustomData(0, ""), CustomData.serializer()).getOrThrow())
    }

    @Test
    fun putValue_ThrowsWithoutSerializer() = runTest(testDispatcher) {
        val custom = CustomData(2, "fail")
        val result = dataStore.putValue("fail", custom)
        assertTrue(result.isFailure)
    }

    @Test
    fun getValue_ThrowsWithoutSerializer() = runTest(testDispatcher) {
        val result = dataStore.getValue("fail", CustomData(0, ""))
        assertTrue(result.isFailure)
    }

    @Test
    fun getValue_ReturnsDefaultIfKeyMissing() = runTest(testDispatcher) {
        assertEquals(99, dataStore.getValue("missing", 99).getOrThrow())
    }

    @Test
    fun hasKey_WorksCorrectly() = runTest(testDispatcher) {
        assertTrue(dataStore.hasKey("nope").getOrThrow() == false)
        assertTrue(dataStore.putValue("exists", 1).isSuccess)
        assertTrue(dataStore.hasKey("exists").getOrThrow())
    }

    @Test
    fun removeValue_RemovesKey() = runTest(testDispatcher) {
        assertTrue(dataStore.putValue("toremove", 5).isSuccess)
        assertTrue(dataStore.removeValue("toremove").isSuccess)
        assertEquals(0, dataStore.getValue("toremove", 0).getOrThrow())
    }

    @Test
    fun clearAll_RemovesAllKeys() = runTest(testDispatcher) {
        assertTrue(dataStore.putValue("a", 1).isSuccess)
        assertTrue(dataStore.putValue("b", 2).isSuccess)
        assertTrue(dataStore.clearAll().isSuccess)
        assertEquals(0, dataStore.getValue("a", 0).getOrThrow())
        assertEquals(0, dataStore.getValue("b", 0).getOrThrow())
    }

    @Test
    fun getAllKeys_ReturnsAllKeys() = runTest(testDispatcher) {
        assertTrue(dataStore.putValue("k1", 1).isSuccess)
        assertTrue(dataStore.putValue("k2", 2).isSuccess)
        assertEquals(setOf("k1", "k2"), dataStore.getAllKeys().getOrThrow())
    }

    @Test
    fun getSize_ReturnsCorrectCount() = runTest(testDispatcher) {
        assertTrue(dataStore.putValue("k1", 1).isSuccess)
        assertTrue(dataStore.putValue("k2", 2).isSuccess)
        assertEquals(2, dataStore.getSize().getOrThrow())
    }
}
