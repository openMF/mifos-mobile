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

import template.core.base.datastore.exceptions.CacheException

/**
 * Least Recently Used (LRU) cache implementation for managing in-memory key-value pairs.
 *
 * This cache automatically evicts the least recently used entry when the maximum size is exceeded.
 * It provides thread-unsafe, fast access for use cases where cache contention is not a concern.
 *
 * Example usage:
 * ```kotlin
 * val cache = LruCacheManager<String, Any>(maxSize = 100)
 * cache.put("theme", "dark")
 * val value = cache.get("theme")
 * cache.remove("theme")
 * cache.clear()
 * val size = cache.size()
 * val exists = cache.containsKey("theme")
 * ```
 *
 * @param K The type of the cache key.
 * @param V The type of the cached value.
 * @property maxSize The maximum number of entries the cache can hold.
 */
class LruCacheManager<K, V>(
    private val maxSize: Int = 100,
) : CacheManager<K, V> {

    private val cache = LinkedHashMap<K, V>(maxSize, 0.75f)

    /**
     * Adds or updates a value in the cache for the specified key.
     * If the cache exceeds its maximum size, the least recently used entry is evicted.
     *
     * @param key The key to associate with the value.
     * @param value The value to store in the cache.
     * @throws CacheException if the operation fails.
     */
    override fun put(key: K, value: V) {
        try {
            cache[key] = value
            if (cache.size > maxSize) {
                val eldest = cache.keys.first()
                cache.remove(eldest)
            }
        } catch (e: Exception) {
            throw CacheException("Failed to put value in cache for key: $key", e)
        }
    }

    /**
     * Retrieves the value associated with the specified key from the cache.
     *
     * @param key The key to look up.
     * @return The value associated with the key, or null if the key is not present.
     * @throws CacheException if the operation fails.
     */
    override fun get(key: K): V? {
        return try {
            cache[key]
        } catch (e: Exception) {
            throw CacheException("Failed to get value from cache for key: $key", e)
        }
    }

    /**
     * Removes the value associated with the specified key from the cache.
     *
     * @param key The key to remove.
     * @return The value that was associated with the key, or null if the key was not present.
     * @throws CacheException if the operation fails.
     */
    override fun remove(key: K): V? {
        return try {
            cache.remove(key)
        } catch (e: Exception) {
            throw CacheException("Failed to remove value from cache for key: $key", e)
        }
    }

    /**
     * Removes all entries from the cache.
     *
     * @throws CacheException if the operation fails.
     */
    override fun clear() {
        try {
            cache.clear()
        } catch (e: Exception) {
            throw CacheException("Failed to clear cache", e)
        }
    }

    /**
     * Returns the current number of entries in the cache.
     *
     * @return The number of key-value pairs currently stored in the cache.
     * @throws CacheException if the operation fails.
     */
    override fun size(): Int {
        return try {
            cache.size
        } catch (e: Exception) {
            throw CacheException("Failed to get cache size", e)
        }
    }

    /**
     * Checks if the cache contains an entry for the specified key.
     *
     * @param key The key to check.
     * @return true if the cache contains an entry for the key, false otherwise.
     * @throws CacheException if the operation fails.
     */
    override fun containsKey(key: K): Boolean {
        return try {
            cache.containsKey(key)
        } catch (e: Exception) {
            throw CacheException("Failed to check if cache contains key: $key", e)
        }
    }
}
