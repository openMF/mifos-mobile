/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:Suppress("MaxLineLength")

package template.core.base.datastore.reactive

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import template.core.base.datastore.repository.ReactivePreferencesRepository

/**
 * Provides advanced operations for combining, mapping, and observing user preference flows.
 *
 * This class enables the combination of multiple preference flows, observation of key changes,
 * and transformation of observed values.
 *
 * Example usage:
 * ```kotlin
 * val operators = PreferenceFlowOperators(repository)
 * val combinedFlow = operators.combinePreferences("key1", 0, "key2", "", transform = { a, b -> "$a-$b" })
 * ```
 *
 * @property repository The [ReactivePreferencesRepository] used to observe and manipulate preference flows.
 */
class PreferenceFlowOperators(
    private val repository: ReactivePreferencesRepository,
) {
    /**
     * Combines two preference flows and emits a value produced by the [transform] function.
     *
     * @param key1 The first preference key.
     * @param default1 The default value for the first preference.
     * @param key2 The second preference key.
     * @param default2 The default value for the second preference.
     * @param transform The function to combine the two values.
     * @return A [Flow] emitting the combined value.
     *
     * Example usage:
     * ```kotlin
     * operators.combinePreferences("theme", "light", "fontSize", 12) { theme, size -> "$theme-$size" }
     * ```
     */
    fun <T1, T2, R> combinePreferences(
        key1: String,
        default1: T1,
        key2: String,
        default2: T2,
        transform: suspend (T1, T2) -> R,
    ): Flow<R> = combine(
        repository.observePreference(key1, default1),
        repository.observePreference(key2, default2),
        transform,
    )

    /**
     * Combines three preference flows and emits a value produced by the [transform] function.
     *
     * @param key1 The first preference key.
     * @param default1 The default value for the first preference.
     * @param key2 The second preference key.
     * @param default2 The default value for the second preference.
     * @param key3 The third preference key.
     * @param default3 The default value for the third preference.
     * @param transform The function to combine the three values.
     * @return A [Flow] emitting the combined value.
     *
     * Example usage:
     * ```kotlin
     * operators.combinePreferences("theme", "light", "fontSize", 12, "lang", "en") { t, s, l -> "$t-$s-$l" }
     * ```
     */
    fun <T1, T2, T3, R> combinePreferences(
        key1: String,
        default1: T1,
        key2: String,
        default2: T2,
        key3: String,
        default3: T3,
        transform: suspend (T1, T2, T3) -> R,
    ): Flow<R> = combine(
        repository.observePreference(key1, default1),
        repository.observePreference(key2, default2),
        repository.observePreference(key3, default3),
        transform,
    )

    /**
     * Observes changes to any of the specified keys and emits the key that changed.
     *
     * @param keys The keys to observe for changes.
     * @return A [Flow] emitting the key that changed.
     *
     * Example usage:
     * ```kotlin
     * operators.observeAnyKeyChange("theme", "fontSize").collect { key -> println("Changed: $key") }
     * ```
     */
    fun observeAnyKeyChange(vararg keys: String): Flow<String> =
        repository.observePreferenceChanges()
            .map { it.key }
            .filter { it in keys }

    /**
     * Observes a preference and maps its value using the provided [transform] function.
     *
     * @param key The preference key to observe.
     * @param default The default value for the preference.
     * @param transform The function to map the preference value.
     * @return A [Flow] emitting the mapped value.
     *
     * Example usage:
     * ```kotlin
     * operators.observeMappedPreference("theme", "light") { it.uppercase() }
     * ```
     */
    fun <T, R> observeMappedPreference(
        key: String,
        default: T,
        transform: suspend (T) -> R,
    ): Flow<R> = repository.observePreference(key, default).map(transform)
}
