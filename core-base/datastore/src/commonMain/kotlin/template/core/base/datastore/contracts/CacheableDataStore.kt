/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.contracts

/**
 * Interface for data storage implementations that support caching.
 *
 * This interface extends the base [DataStore] interface, adding operations for managing
 * an in-memory cache associated with the stored data.
 *
 * Example usage:
 * ```kotlin
 * val cacheableDataStore: CacheableDataStore = ...
 * cacheableDataStore.putValue("settings_cache", "enabled")
 * val value = cacheableDataStore.getValue("settings_cache", "disabled")
 * cacheableDataStore.invalidateCache("settings_cache")
 * ```
 */
interface CacheableDataStore : DataStore {
    /**
     * Stores a value associated with the specified key in the data store and updates the cache.
     *
     * @param key The key to associate with the value.
     * @param value The value to store.
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     */
    suspend fun <T> putValue(key: String, value: T): Result<Unit>

    /**
     * Retrieves a value associated with the specified key, checking the cache first.
     *
     * @param key The key to retrieve.
     * @param default The default value to return if the key does not exist in the data store.
     * @return [Result.success] with the value, or [Result.failure] if an error occurs.
     */
    suspend fun <T> getValue(key: String, default: T): Result<T>

    /**
     * Stores a serializable value using the provided serializer and updates the cache.
     *
     * @param key The key to associate with the value.
     * @param value The value to store.
     * @param serializer The serializer for the value type.
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     */
    suspend fun <T> putSerializableValue(
        key: String,
        value: T,
        serializer: kotlinx.serialization.KSerializer<T>,
    ): Result<Unit>

    /**
     * Retrieves a serializable value using the provided serializer, checking the cache first.
     *
     * @param key The key to retrieve.
     * @param default The default value to return if the key does not exist in the data store.
     * @param serializer The serializer for the value type.
     * @return [Result.success] with the value, or [Result.failure] if an error occurs.
     */
    suspend fun <T> getSerializableValue(
        key: String,
        default: T,
        serializer: kotlinx.serialization.KSerializer<T>,
    ): Result<T>

    /**
     * Invalidates the cache entry for the specified key.
     *
     * This forces the next retrieval for this key to read from the underlying data store.
     *
     * @param key The key whose cache entry should be invalidated.
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     */
    suspend fun invalidateCache(key: String): Result<Unit>

    /**
     * Invalidates all entries in the cache.
     *
     * This forces the next retrieval for any key to read from the underlying data store.
     *
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     */
    suspend fun invalidateAllCache(): Result<Unit>

    /**
     * Returns the current number of entries in the cache.
     *
     * @return The size of the cache.
     */
    fun getCacheSize(): Int
}
