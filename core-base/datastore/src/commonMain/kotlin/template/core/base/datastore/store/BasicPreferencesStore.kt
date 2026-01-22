/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.store

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.serialization.decodeValue
import com.russhwolf.settings.serialization.encodeValue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer

/**
 * Basic implementation of a user preferences data store using a settings storage mechanism.
 *
 * This class is deprecated. Use [ReactiveUserPreferencesDataStore]
 * via [template.core.base.datastore.factory.DataStoreFactory] for advanced features such
 * as reactive flows, caching, and validation.
 *
 * Example migration:
 * ```kotlin
 * // Old way
 * val dataStore = BasicPreferencesStore(settings, dispatcher)
 *
 * // New way
 * val repository = DataStoreFactory.create(settings, dispatcher)
 * ```
 *
 * @property settings The underlying settings storage implementation.
 * @property dispatcher The coroutine dispatcher for executing operations.
 */
@Deprecated(
    message = "Use ReactiveUserPreferencesRepository through DataStoreFactory instead",
    replaceWith = ReplaceWith(
        "DataStoreFactory.create(settings, dispatcher)",
        "template.core.base.datastore.factory.DataStoreFactory",
    ),
    level = DeprecationLevel.WARNING,
)
class BasicPreferencesStore(
    private val settings: Settings,
    private val dispatcher: CoroutineDispatcher,
) {

    /**
     * Stores a value associated with the specified key in the data store.
     * Supports primitive types directly and custom types via a provided [KSerializer].
     *
     * @param key The key to associate with the value.
     * @param value The value to store.
     * @param serializer The serializer for the value type, if needed.
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * dataStore.putValue("theme", "dark")
     * ```
     */
    @OptIn(ExperimentalSerializationApi::class, ExperimentalSettingsApi::class)
    suspend fun <T> putValue(
        key: String,
        value: T,
        serializer: KSerializer<T>? = null,
    ): Result<Unit> = withContext(dispatcher) {
        runCatching {
            when (value) {
                is Int -> settings.putInt(key, value)
                is Long -> settings.putLong(key, value)
                is Float -> settings.putFloat(key, value)
                is Double -> settings.putDouble(key, value)
                is String -> settings.putString(key, value)
                is Boolean -> settings.putBoolean(key, value)
                else -> {
                    require(serializer != null) {
                        "Unsupported type or no serializer provided for ${value?.let { it::class } ?: "null"}"
                    }
                    settings.encodeValue(
                        serializer = serializer,
                        value = value,
                        key = key,
                    )
                }
            }
        }
    }

    /**
     * Retrieves a value associated with the specified key from the data store.
     *
     * @param key The key to retrieve.
     * @param default The default value to return if the key does not exist.
     * @param serializer The serializer for the value type, if needed.
     * @return [Result.success] with the value, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * dataStore.getValue("theme", "light")
     * ```
     */
    @Suppress("UNCHECKED_CAST")
    @OptIn(ExperimentalSerializationApi::class, ExperimentalSettingsApi::class)
    suspend fun <T> getValue(
        key: String,
        default: T,
        serializer: KSerializer<T>? = null,
    ): Result<T> = withContext(dispatcher) {
        runCatching {
            when (default) {
                is Int -> settings.getInt(key, default) as T
                is Long -> settings.getLong(key, default) as T
                is Float -> settings.getFloat(key, default) as T
                is Double -> settings.getDouble(key, default) as T
                is String -> settings.getString(key, default) as T
                is Boolean -> settings.getBoolean(key, default) as T
                else -> {
                    require(serializer != null) {
                        "Unsupported type or no serializer provided for ${default?.let { it::class } ?: "null"}"
                    }
                    settings.decodeValue(
                        serializer = serializer,
                        key = key,
                        defaultValue = default,
                    )
                }
            }
        }
    }

    /**
     * Checks if the specified key exists in the data store.
     *
     * @param key The key to check.
     * @return [Result.success] with true if the key exists, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * dataStore.hasKey("theme")
     * ```
     */
    suspend fun hasKey(key: String): Result<Boolean> = withContext(dispatcher) {
        runCatching { settings.hasKey(key) }
    }

    /**
     * Removes the value associated with the specified key from the data store.
     *
     * @param key The key to remove.
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * dataStore.removeValue("theme")
     * ```
     */
    suspend fun removeValue(key: String): Result<Unit> = withContext(dispatcher) {
        runCatching { settings.remove(key) }
    }

    /**
     * Clears all stored preferences in the data store.
     *
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * dataStore.clearAll()
     * ```
     */
    suspend fun clearAll(): Result<Unit> = withContext(dispatcher) {
        runCatching { settings.clear() }
    }

    /**
     * Retrieves all keys currently stored in the data store.
     *
     * @return [Result.success] with the set of keys, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * dataStore.getAllKeys()
     * ```
     */
    suspend fun getAllKeys(): Result<Set<String>> = withContext(dispatcher) {
        runCatching { settings.keys }
    }

    /**
     * Returns the total number of key-value pairs stored in the data store.
     *
     * @return [Result.success] with the count, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * dataStore.getSize()
     * ```
     */
    suspend fun getSize(): Result<Int> = withContext(dispatcher) {
        runCatching { settings.size }
    }
}
