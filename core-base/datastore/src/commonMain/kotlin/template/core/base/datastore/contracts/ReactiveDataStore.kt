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

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer

/**
 * A reactive interface for observing changes in a data store.
 *
 * This interface extends [CacheableDataStore] and provides methods to observe changes in the data store
 * using Kotlin Flow. It enables reactive programming patterns by emitting updates whenever the underlying
 * data changes.
 *
 * Example usage:
 * ```kotlin
 * // Observe a simple value
 * dataStore.observeValue("theme", "light")
 *     .collect { theme -> println("Theme changed to: $theme") }
 *
 * // Observe a serializable value
 * dataStore.observeSerializableValue("user", defaultUser, User.serializer())
 *     .collect { user -> println("User updated: $user") }
 *
 * // Observe all keys
 * dataStore.observeKeys()
 *     .collect { keys -> println("Available keys: $keys") }
 * ```
 */
interface ReactiveDataStore : CacheableDataStore {
    /**
     * Observes changes to a value associated with the specified key.
     *
     * @param key The key to observe.
     * @param default The default value to emit if the key does not exist.
     * @return A [Flow] that emits the current value and subsequent updates.
     */
    fun <T> observeValue(key: String, default: T): Flow<T>

    /**
     * Observes changes to a serializable value associated with the specified key.
     *
     * @param key The key to observe.
     * @param default The default value to emit if the key does not exist.
     * @param serializer The serializer for the value type.
     * @return A [Flow] that emits the current value and subsequent updates.
     */
    fun <T> observeSerializableValue(
        key: String,
        default: T,
        serializer: KSerializer<T>,
    ): Flow<T>

    /**
     * Observes changes to the set of keys in the data store.
     *
     * @return A [Flow] that emits the current set of keys and subsequent updates.
     */
    fun observeKeys(): Flow<Set<String>>

    /**
     * Observes changes to the total number of entries in the data store.
     *
     * @return A [Flow] that emits the current size and subsequent updates.
     */
    fun observeSize(): Flow<Int>

    /**
     * Observes all changes that occur in the data store.
     *
     * @return A [Flow] that emits [DataStoreChangeEvent] instances for all changes.
     */
    fun observeChanges(): Flow<DataStoreChangeEvent>
}
