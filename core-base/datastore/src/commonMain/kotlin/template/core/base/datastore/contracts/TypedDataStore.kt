/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.contracts

import kotlinx.serialization.KSerializer

/**
 * A type-safe interface for managing persistent data storage operations.
 *
 * This interface extends [DataStore] and provides methods for storing and retrieving typed data
 * with proper serialization support. It ensures type safety through generic parameters and
 * serialization mechanisms.
 *
 * Example usage:
 * ```kotlin
 * // Store a simple value
 * dataStore.putValue("theme", "dark")
 *
 * // Store a serializable object
 * dataStore.putSerializableValue("user", user, User.serializer())
 *
 * // Retrieve values
 * val theme = dataStore.getValue("theme", "light")
 * val user = dataStore.getSerializableValue("user", defaultUser, User.serializer())
 * ```
 *
 * @param T The generic type parameter representing the data type to be stored
 * @param K The generic type parameter representing the key type used for data identification
 */
interface TypedDataStore : DataStore {
    /**
     * Stores a value of type [T] associated with the specified key.
     *
     * @param key The unique identifier for the value
     * @param value The value to be stored
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs
     */
    suspend fun <T> putValue(key: String, value: T): Result<Unit>

    /**
     * Retrieves a value of type [T] associated with the specified key.
     *
     * @param key The unique identifier for the value
     * @param default The default value to return if the key does not exist
     * @return [Result.success] containing the retrieved value, or [Result.failure] if an error occurs
     */
    suspend fun <T> getValue(key: String, default: T): Result<T>

    /**
     * Stores a serializable value of type [T] using the provided serializer.
     *
     * @param key The unique identifier for the value
     * @param value The serializable value to be stored
     * @param serializer The [KSerializer] for type [T]
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs
     */
    suspend fun <T> putSerializableValue(
        key: String,
        value: T,
        serializer: KSerializer<T>,
    ): Result<Unit>

    /**
     * Retrieves a serializable value of type [T] using the provided serializer.
     *
     * @param key The unique identifier for the value
     * @param default The default value to return if the key does not exist
     * @param serializer The [KSerializer] for type [T]
     * @return [Result.success] containing the deserialized value, or [Result.failure] if an error occurs
     */
    suspend fun <T> getSerializableValue(
        key: String,
        default: T,
        serializer: KSerializer<T>,
    ): Result<T>
}
