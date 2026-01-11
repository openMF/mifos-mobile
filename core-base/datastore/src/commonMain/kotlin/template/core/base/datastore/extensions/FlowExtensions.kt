/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.extensions

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import template.core.base.datastore.contracts.DataStoreChangeEvent

/**
 * Provides extension functions for Kotlin [Flow] related to data store operations.
 *
 * These extensions offer convenient ways to work with preference flows,
 * such as mapping values with default handling, filtering by change type, and logging changes.
 */

/**
 * Maps values emitted by the flow using the [transform] function and catches any errors,
 * emitting the [default] value in case of failure.
 *
 * @param default The default value to emit if the transformation or upstream flow encounters an error.
 * @param transform The function to apply to each value emitted by the flow.
 * @return A [Flow] emitting the transformed values or the default value on error.
 *
 * Example usage:
 * ```kotlin
 * flowOf("1", "2", "abc") // Example flow of strings
 *     .mapWithDefault(0) { it.toInt() } // Transform to Int, use 0 if parsing fails
 *     .collect { value -> println(value) } // Prints 1, 2, 0
 * ```
 */
fun <T, R> Flow<T>.mapWithDefault(default: R, transform: (T) -> R): Flow<R> {
    return this.map { transform(it) }
        .catch { emit(default) }
}

/**
 * Filters a flow of [DataStoreChangeEvent] instances to emit only events of a specific type [T].
 *
 * This is useful for reacting only to specific changes like value additions or removals.
 *
 * @param T The specific type of [DataStoreChangeEvent] to filter for.
 * @return A [Flow] emitting only [DataStoreChangeEvent] instances of type [T].
 *
 * Example usage:
 * ```kotlin
 * dataStore.observeChanges()
 *     .filterChangeType<DataStoreChangeEvent.ValueAdded>()
 *     .collect { event -> println("New value added: ${event.key}") }
 * ```
 */
inline fun <reified T : DataStoreChangeEvent> Flow<DataStoreChangeEvent>.filterChangeType(): Flow<T> {
    return this.map { it as? T }.filter { it != null }.map { it!! }
}

/**
 * Filters a flow of [DataStoreChangeEvent] instances to emit only [ValueAdded] events.
 *
 * This is a convenience function equivalent to `filterChangeType<DataStoreChangeEvent.ValueAdded>()`.
 *
 * @return A [Flow] emitting only [DataStoreChangeEvent.ValueAdded] instances.
 *
 * Example usage:
 * ```kotlin
 * dataStore.observeChanges()
 *     .onlyAdditions()
 *     .collect { event -> println("Value added: ${event.key}") }
 * ```
 */
fun Flow<DataStoreChangeEvent>.onlyAdditions(): Flow<DataStoreChangeEvent.ValueAdded> {
    return filterChangeType()
}

/**
 * Filters a flow of [DataStoreChangeEvent] instances to emit only [ValueUpdated] events.
 *
 * This is a convenience function equivalent to `filterChangeType<DataStoreChangeEvent.ValueUpdated>()`.
 *
 * @return A [Flow] emitting only [DataStoreChangeEvent.ValueUpdated] instances.
 *
 * Example usage:
 * ```kotlin
 * dataStore.observeChanges()
 *     .onlyUpdates()
 *     .collect { event -> println("Value updated: ${event.key}") }
 * ```
 */
fun Flow<DataStoreChangeEvent>.onlyUpdates(): Flow<DataStoreChangeEvent.ValueUpdated> {
    return filterChangeType()
}

/**
 * Filters a flow of [DataStoreChangeEvent] instances to emit only [ValueRemoved] events.
 *
 * This is a convenience function equivalent to `filterChangeType<DataStoreChangeEvent.ValueRemoved>()`.
 *
 * @return A [Flow] emitting only [DataStoreChangeEvent.ValueRemoved] instances.
 *
 * Example usage:
 * ```kotlin
 * dataStore.observeChanges()
 *     .onlyRemovals()
 *     .collect { event -> println("Value removed: ${event.key}") }
 * ```
 */
fun Flow<DataStoreChangeEvent>.onlyRemovals(): Flow<DataStoreChangeEvent.ValueRemoved> {
    return filterChangeType()
}

/**
 * Debounces a flow of preference changes to avoid excessive emissions.
 *
 * This uses [distinctUntilChanged] as a simple debouncing mechanism. For true time-based debouncing,
 * consider using a library like `kotlinx-coroutines-core` with its `debounce` operator.
 *
 * @param timeoutMillis The timeout duration in milliseconds (currently not used for time-based debouncing).
 * @return A [Flow] that suppresses consecutive duplicate emissions.
 *
 * Example usage:
 * ```kotlin
 * preferencesFlow
 *     .debouncePreferences(300) // Suppress rapid identical emissions
 *     .collect { value -> println("Debounced value: $value") }
 * ```
 */
@OptIn(FlowPreview::class)
fun <T> Flow<T>.debouncePreferences(timeoutMillis: Long = 300): Flow<T> {
    // Note: This would require kotlinx-coroutines-core with debounce support
    // For now, we'll use distinctUntilChanged as a simple approach
    return this.debounce(timeoutMillis)
}

/**
 * Logs each value emitted by the flow for debugging purposes.
 *
 * @param tag A tag to include in the log output.
 * @return The original [Flow].
 *
 * Example usage:
 * ```kotlin
 * preferencesFlow
 *     .logChanges("MyAppPrefs")
 *     .collect { value -> // Process value }
 * ```
 */
fun <T> Flow<T>.logChanges(tag: String = "DataStore"): Flow<T> {
    return this.onEach { value ->
        println("[$tag] Value changed: $value")
    }
}
