/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.store

import template.core.base.common.DataState
import kotlin.time.Duration
import kotlin.time.TimeSource

/**
 * Wraps data with metadata about its origin, freshness, and refresh state.
 *
 * Works identically for both source modes:
 * - **Network + Cache**: emits cached data first (fast), then network data (updated)
 * - **Network only**: emits network data directly, no cache/staleness
 *
 * Feature modules collect `Flow<StoreData<T>>` from repositories and render
 * cache-then-refresh UX patterns without knowing the source mode.
 *
 * @param T The domain data type.
 * @param data The actual data payload.
 * @param origin Where this data emission came from.
 * @param isRefreshing True when a network fetch is in progress (cached data is being shown).
 * @param fetchedAt Monotonic time mark of the last successful network fetch, or null if never fetched.
 * @param error Non-null if the most recent refresh attempt failed. Data may still be valid (stale).
 * @param isEmpty True if the data represents an empty result (e.g., empty list, null-equivalent).
 */
data class StoreData<out T>(
    val data: T,
    val origin: DataOrigin,
    val isRefreshing: Boolean = false,
    val fetchedAt: TimeSource.Monotonic.ValueTimeMark? = null,
    val error: Throwable? = null,
    val isEmpty: Boolean = false,
) {
    /** True if data has never been fetched from network (only meaningful for cached stores). */
    val isStale: Boolean get() = fetchedAt == null && origin != DataOrigin.NETWORK

    /** Duration since last successful network fetch, or null if never fetched. */
    val staleDuration: Duration? get() = fetchedAt?.elapsedNow()

    /** True if this emission has data and no error. */
    val isSuccess: Boolean get() = error == null && !isEmpty

    /** True if this is a terminal error with no usable data. */
    val isError: Boolean get() = error != null && isEmpty
}

/**
 * Indicates where a [StoreData] emission originated.
 */
enum class DataOrigin {
    /** Data was read from local persistence (Room database). */
    CACHE,

    /** Data was freshly fetched from the network (API). */
    NETWORK,

    /** Data was served from Store's in-memory cache. */
    MEMORY,
}

/**
 * Converts [StoreData] to [DataState] for ViewModels using DataState patterns.
 *
 * Mapping:
 * - [isEmpty] + no error -> [DataState.Loading] (no data yet)
 * - [isRefreshing] + data -> [DataState.Pending] (cached data, refresh in progress)
 * - [error] != null -> [DataState.Error] (with optional stale data)
 * - data present, no error -> [DataState.Success]
 */
fun <T> StoreData<T>.toDataState(): DataState<T> {
    return when {
        isEmpty && error == null -> DataState.Loading
        error != null -> DataState.Error(error, if (isEmpty) null else data)
        isRefreshing -> DataState.Pending(data)
        else -> DataState.Success(data)
    }
}
