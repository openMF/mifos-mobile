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

import kotlinx.serialization.KSerializer

/**
 * Interface for managing user preferences in a type-safe and coroutine-friendly manner.
 *
 * Implementations of this interface provide methods for saving, retrieving,
 * and removing preferences, supporting both primitive and serializable types.
 *
 * Example usage:
 * ```kotlin
 * val repository: PreferencesRepository = ...
 * repository.savePreference("theme", "dark")
 * val theme = repository.getPreference("theme", "light")
 * repository.removePreference("theme")
 * repository.clearAllPreferences()
 * val exists = repository.hasPreference("theme")
 * ```
 */
interface PreferencesRepository {
    /**
     * Saves a value associated with the specified key in the preferences.
     *
     * @param key The key to associate with the value.
     * @param value The value to store.
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     */
    suspend fun <T> savePreference(key: String, value: T): Result<Unit>

    /**
     * Retrieves a value associated with the specified key from the preferences.
     *
     * @param key The key to retrieve.
     * @param default The default value to return if the key does not exist.
     * @return [Result.success] with the value, or [Result.failure] if an error occurs.
     */
    suspend fun <T> getPreference(key: String, default: T): Result<T>

    /**
     * Saves a serializable value using the provided serializer.
     *
     * @param key The key to associate with the value.
     * @param value The value to store.
     * @param serializer The serializer for the value type.
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     */
    suspend fun <T> saveSerializablePreference(
        key: String,
        value: T,
        serializer: KSerializer<T>,
    ): Result<Unit>

    /**
     * Retrieves a serializable value using the provided serializer.
     *
     * @param key The key to retrieve.
     * @param default The default value to return if the key does not exist.
     * @param serializer The serializer for the value type.
     * @return [Result.success] with the value, or [Result.failure] if an error occurs.
     */
    suspend fun <T> getSerializablePreference(
        key: String,
        default: T,
        serializer: KSerializer<T>,
    ): Result<T>

    /**
     * Removes the value associated with the specified key from the preferences.
     *
     * @param key The key to remove.
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     */
    suspend fun removePreference(key: String): Result<Unit>

    /**
     * Clears all stored preferences.
     *
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     */
    suspend fun clearAllPreferences(): Result<Unit>

    /**
     * Checks if the specified key exists in the preferences.
     *
     * @param key The key to check.
     * @return `true` if the key exists, `false` otherwise.
     */
    suspend fun hasPreference(key: String): Boolean
}
