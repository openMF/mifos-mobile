/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.handlers

import com.russhwolf.settings.Settings

/**
 * Interface for handling type conversions and storage operations in the data store.
 *
 * Implementations of this interface provide methods for storing, retrieving,
 * and checking support for specific types in the underlying settings storage.
 *
 * Example usage:
 * ```kotlin
 * val handler: TypeHandler<Int> = IntTypeHandler()
 * handler.put(settings, "count", 42)
 * val value = handler.get(settings, "count", 0)
 * ```
 *
 * @param T The type to be handled by this handler.
 */
interface TypeHandler<T> {
    /**
     * Stores a value of type [T] in the settings storage.
     *
     * @param settings The settings storage instance.
     * @param key The key to associate with the value.
     * @param value The value to store.
     * @return [Result.success] if the operation succeeds, or [Result.failure] if an error occurs.
     */
    suspend fun put(settings: Settings, key: String, value: T): Result<Unit>

    /**
     * Retrieves a value of type [T] from the settings storage.
     *
     * @param settings The settings storage instance.
     * @param key The key to retrieve.
     * @param default The default value to return if the key does not exist.
     * @return [Result.success] with the value, or [Result.failure] if an error occurs.
     */
    suspend fun get(settings: Settings, key: String, default: T): Result<T>

    /**
     * Determines whether this handler can process the given value.
     *
     * @param value The value to check.
     * @return `true` if this handler can process the value, `false` otherwise.
     */
    fun canHandle(value: Any?): Boolean
}
