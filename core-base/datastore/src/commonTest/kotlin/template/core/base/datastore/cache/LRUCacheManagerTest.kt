/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.cache

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LRUCacheManagerTest {
    @Test
    fun putAndGet_WorksCorrectly() {
        val cache = LruCacheManager<String, Int>(maxSize = 2)
        cache.put("a", 1)
        assertEquals(1, cache.get("a"))
    }

    @Test
    fun remove_RemovesKey() {
        val cache = LruCacheManager<String, Int>(maxSize = 2)
        cache.put("a", 1)
        cache.remove("a")
        assertNull(cache.get("a"))
    }

    @Test
    fun clear_RemovesAll() {
        val cache = LruCacheManager<String, Int>(maxSize = 2)
        cache.put("a", 1)
        cache.put("b", 2)
        cache.clear()
        assertEquals(0, cache.size())
    }

    @Test
    fun eviction_EvictsLeastRecentlyUsed() {
        val cache = LruCacheManager<String, Int>(maxSize = 2)
        cache.put("a", 1)
        cache.put("b", 2)
        cache.put("c", 3) // Should evict "a"
        assertNull(cache.get("a"))
        assertEquals(2, cache.size())
    }

    @Test
    fun containsKey_Works() {
        val cache = LruCacheManager<String, Int>(maxSize = 2)
        cache.put("a", 1)
        assertTrue(cache.containsKey("a"))
        cache.remove("a")
        assertTrue(!cache.containsKey("a"))
    }
}
