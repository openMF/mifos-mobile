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

import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import template.core.base.datastore.cache.CacheManager
import template.core.base.datastore.contracts.CacheableDataStore
import template.core.base.datastore.exceptions.CacheException
import template.core.base.datastore.exceptions.UnsupportedTypeException
import template.core.base.datastore.handlers.TypeHandler
import template.core.base.datastore.serialization.SerializationStrategy
import template.core.base.datastore.validation.PreferencesValidator

/**
 * Implementation of a cache-enabled user preferences data store.
 *
 * This class provides coroutine-based, type-safe, and observable access to user preferences,
 * supporting both primitive and serializable types, with in-memory caching for improved performance.
 *
 * Example usage:
 * ```kotlin
 * val store = CachedPreferencesStore(
 *     settings = Settings(),
 *     dispatcher = Dispatchers.IO,
 *     typeHandlers = listOf(IntTypeHandler(), StringTypeHandler()),
 *     serializationStrategy = JsonSerializationStrategy(),
 *     validator = DefaultPreferencesValidator(),
 *     cacheManager = LruCacheManager(200)
 * )
 * ```
 *
 * @property settings The underlying settings storage implementation.
 * @property dispatcher The coroutine dispatcher for executing operations.
 * @property typeHandlers The list of type handlers for supported types.
 * @property serializationStrategy The strategy for serializing and deserializing values.
 * @property validator The validator for keys and values.
 * @property cacheManager The cache manager for in-memory caching.
 */
@Suppress("MaxLineLength")
class CachedPreferencesStore(
    private val settings: Settings,
    private val dispatcher: CoroutineDispatcher,
    private val typeHandlers: List<TypeHandler<Any>>,
    private val serializationStrategy: SerializationStrategy,
    private val validator: PreferencesValidator,
    private val cacheManager: CacheManager<String, Any>,
) : CacheableDataStore {

    /**
     * Stores a value associated with the specified key in the data store.
     *
     * @param key The key to associate with the value.
     * @param value The value to store.
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * store.putValue("theme", "dark")
     * ```
     */
    override suspend fun <T> putValue(key: String, value: T): Result<Unit> =
        withContext(dispatcher) {
            runCatching {
                // Validate inputs
                validator.validateKey(key).getOrThrow()
                validator.validateValue(value).getOrThrow()

                // Find and use type handler
                val handler = findTypeHandler(value) ?: throw UnsupportedTypeException(
                    "No handler found for type: ${value?.let { it::class.simpleName } ?: "null"}",
                )

                @Suppress("UNCHECKED_CAST")
                val typedHandler = handler as TypeHandler<T>
                val result = typedHandler.put(settings, key, value)

                if (result.isSuccess) {
                    cacheValue(key, value as Any)
                }

                result.getOrThrow()
            }
        }

    /**
     * Retrieves a value associated with the specified key from the data store.
     *
     * @param key The key to retrieve.
     * @param default The default value to return if the key does not exist.
     * @return [Result.success] with the value, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * store.getValue("theme", "light")
     * ```
     */
    override suspend fun <T> getValue(key: String, default: T): Result<T> =
        withContext(dispatcher) {
            runCatching {
                validator.validateKey(key).getOrThrow()

                // Check cache first
                getCachedValue<T>(key)?.let { return@runCatching it }

                // Find and use type handler
                val handler = findTypeHandler(default)
                    ?: throw UnsupportedTypeException(
                        "No handler found for type: ${default?.let { it::class.simpleName } ?: "null"}",
                    )

                @Suppress("UNCHECKED_CAST")
                val typedHandler = handler as TypeHandler<T>
                val result = typedHandler.get(settings, key, default)

                if (result.isSuccess) {
                    cacheValue(key, result.getOrThrow() as Any)
                }

                result.getOrThrow()
            }
        }

    /**
     * Stores a serializable value using the provided serializer.
     *
     * @param key The key to associate with the value.
     * @param value The value to store.
     * @param serializer The serializer for the value type.
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * store.putSerializableValue("user", user, User.serializer())
     * ```
     */
    override suspend fun <T> putSerializableValue(
        key: String,
        value: T,
        serializer: KSerializer<T>,
    ): Result<Unit> = withContext(dispatcher) {
        runCatching {
            validator.validateKey(key).getOrThrow()
            validator.validateValue(value).getOrThrow()

            val serializedResult = serializationStrategy.serialize(value, serializer)
            serializedResult.fold(
                onSuccess = { serializedData ->
                    val result = runCatching { settings.putString(key, serializedData) }
                    if (result.isSuccess) {
                        cacheValue(key, value as Any)
                    }
                    result.getOrThrow()
                },
                onFailure = { throw it },
            )
        }
    }

    /**
     * Retrieves a serializable value using the provided serializer.
     *
     * @param key The key to retrieve.
     * @param default The default value to return if the key does not exist.
     * @param serializer The serializer for the value type.
     * @return [Result.success] with the value, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * store.getSerializableValue("user", defaultUser, User.serializer())
     * ```
     */
    override suspend fun <T> getSerializableValue(
        key: String,
        default: T,
        serializer: KSerializer<T>,
    ): Result<T> = withContext(dispatcher) {
        runCatching {
            validator.validateKey(key).getOrThrow()

            // Check cache first
            getCachedValue<T>(key)?.let { return@runCatching it }

            if (!settings.hasKey(key)) {
                cacheValue(key, default as Any)
                return@runCatching default
            }

            val serializedData = runCatching { settings.getString(key, "") }
                .getOrElse { throw it }

            if (serializedData.isEmpty()) {
                return@runCatching default
            }

            serializationStrategy.deserialize(serializedData, serializer).fold(
                onSuccess = { value ->
                    cacheValue(key, value as Any)
                    value
                },
                onFailure = {
                    // Return default on deserialization failure but don't cache it
                    default
                },
            )
        }
    }

    /**
     * Checks if the specified key exists in the data store or cache.
     *
     * @param key The key to check.
     * @return [Result.success] with true if the key exists, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * store.hasKey("theme")
     * ```
     */
    override suspend fun hasKey(key: String): Result<Boolean> = withContext(dispatcher) {
        runCatching {
            validator.validateKey(key).getOrThrow()
            settings.hasKey(key) || cacheManager.containsKey(key)
        }
    }

    /**
     * Removes the value associated with the specified key from the data store and cache.
     *
     * @param key The key to remove.
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * store.removeValue("theme")
     * ```
     */
    override suspend fun removeValue(key: String): Result<Unit> = withContext(dispatcher) {
        runCatching {
            validator.validateKey(key).getOrThrow()
            settings.remove(key)
            cacheManager.remove(key)
            Unit
        }
    }

    /**
     * Clears all stored preferences in the data store and cache.
     *
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * store.clearAll()
     * ```
     */
    override suspend fun clearAll(): Result<Unit> = withContext(dispatcher) {
        runCatching {
            settings.clear()
            cacheManager.clear()
        }
    }

    /**
     * Retrieves all keys currently stored in the data store.
     *
     * @return [Result.success] with the set of keys, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * store.getAllKeys()
     * ```
     */
    override suspend fun getAllKeys(): Result<Set<String>> = withContext(dispatcher) {
        runCatching { settings.keys }
    }

    /**
     * Retrieves the total number of key-value pairs stored in the data store.
     *
     * @return [Result.success] with the count, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * store.getSize()
     * ```
     */
    override suspend fun getSize(): Result<Int> = withContext(dispatcher) {
        runCatching { settings.size }
    }

    /**
     * Invalidates the cache for the specified key.
     *
     * @param key The key whose cache should be invalidated.
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * store.invalidateCache("theme")
     * ```
     */
    override suspend fun invalidateCache(key: String): Result<Unit> = withContext(dispatcher) {
        runCatching {
            validator.validateKey(key).getOrThrow()
            cacheManager.remove(key)
            Unit
        }
    }

    /**
     * Invalidates all cache entries in the data store.
     *
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     *
     * Example usage:
     * ```kotlin
     * store.invalidateAllCache()
     * ```
     */
    override suspend fun invalidateAllCache(): Result<Unit> = withContext(dispatcher) {
        runCatching {
            cacheManager.clear()
        }
    }

    /**
     * Returns the current size of the cache.
     *
     * @return The number of entries in the cache.
     *
     * Example usage:
     * ```kotlin
     * val cacheSize = store.getCacheSize()
     * ```
     */
    override fun getCacheSize(): Int = cacheManager.size()

    // Private helper methods to reduce duplication
    private fun findTypeHandler(value: Any?): TypeHandler<Any>? {
        return typeHandlers.find { it.canHandle(value) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getCachedValue(key: String): T? {
        return try {
            cacheManager.get(key) as? T
        } catch (_: Exception) {
            // Log cache retrieval error but don't fail the operation
            null
        }
    }

    private fun cacheValue(key: String, value: Any) {
        try {
            cacheManager.put(key, value)
        } catch (e: Exception) {
            // Cache operation failed - log but don't fail the main operation
            throw CacheException("Failed to cache value for key: $key", e)
        }
    }
}
