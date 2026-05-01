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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin
import kotlin.time.TimeSource

/**
 * Maps a [StoreReadResponse] flow into `Flow<StoreData<Output>>`.
 *
 * Works for both source modes:
 * - **Network + Cache store**: Loading -> Data(Cache, refreshing) -> Data(Network)
 * - **Network-only store**: Loading -> Data(Network) or Error
 *
 * @param isEmpty Optional predicate to detect empty results (e.g., `{ it.isEmpty() }` for lists).
 *   Defaults to false (all data is non-empty).
 */
fun <Output : Any> Flow<StoreReadResponse<Output>>.mapToStoreData(
    isEmpty: (Output) -> Boolean = { false },
): Flow<StoreData<Output>> {
    var refreshing = false
    var lastFetchMark: TimeSource.Monotonic.ValueTimeMark? = null

    return transform { response ->
        when (response) {
            is StoreReadResponse.Initial,
            is StoreReadResponse.Loading,
            -> {
                refreshing = true
            }

            is StoreReadResponse.Data -> {
                val origin = response.origin.toDataOrigin()
                if (origin == DataOrigin.NETWORK) {
                    lastFetchMark = TimeSource.Monotonic.markNow()
                    refreshing = false
                }
                emit(
                    StoreData(
                        data = response.value,
                        origin = origin,
                        isRefreshing = refreshing && origin != DataOrigin.NETWORK,
                        fetchedAt = lastFetchMark,
                        isEmpty = isEmpty(response.value),
                    ),
                )
            }

            is StoreReadResponse.NoNewData -> {
                refreshing = false
            }

            is StoreReadResponse.Error -> {
                refreshing = false
            }
        }
    }
}

/**
 * Like [mapToStoreData] but also emits on errors, carrying the last known data.
 *
 * Handles all status scenarios:
 * - **Success**: Data arrives -> emit with origin and staleness
 * - **Empty**: Data arrives but isEmpty predicate is true -> emit with isEmpty=true
 * - **Error with stale data**: Error after cache -> emit error with last known data
 * - **Error without data**: Error before any data -> emit error with fallback (isEmpty=true)
 * - **No network**: Error type hints at connectivity -> ViewModel can check error type
 *
 * @param fallback Value to use if an error arrives before any data.
 * @param isEmpty Predicate to detect empty results.
 */
fun <Output : Any> Flow<StoreReadResponse<Output>>.mapToStoreDataWithErrors(
    fallback: Output,
    isEmpty: (Output) -> Boolean = { false },
): Flow<StoreData<Output>> {
    var refreshing = false
    var lastFetchMark: TimeSource.Monotonic.ValueTimeMark? = null
    var lastData: Output = fallback
    var hasReceivedData = false

    return transform { response ->
        when (response) {
            is StoreReadResponse.Initial,
            is StoreReadResponse.Loading,
            -> {
                refreshing = true
            }

            is StoreReadResponse.Data -> {
                val origin = response.origin.toDataOrigin()
                if (origin == DataOrigin.NETWORK) {
                    lastFetchMark = TimeSource.Monotonic.markNow()
                    refreshing = false
                }
                lastData = response.value
                hasReceivedData = true
                emit(
                    StoreData(
                        data = response.value,
                        origin = origin,
                        isRefreshing = refreshing && origin != DataOrigin.NETWORK,
                        fetchedAt = lastFetchMark,
                        isEmpty = isEmpty(response.value),
                    ),
                )
            }

            is StoreReadResponse.NoNewData -> {
                refreshing = false
            }

            is StoreReadResponse.Error -> {
                refreshing = false
                val error = response.toThrowable()
                emit(
                    StoreData(
                        data = lastData,
                        origin = if (hasReceivedData) DataOrigin.CACHE else DataOrigin.NETWORK,
                        isRefreshing = false,
                        fetchedAt = lastFetchMark,
                        error = error,
                        isEmpty = !hasReceivedData,
                    ),
                )
            }
        }
    }
}

/**
 * Maps [StoreReadResponseOrigin] to [DataOrigin].
 *
 * [StoreReadResponseOrigin] is a top-level sealed class (NOT nested in StoreReadResponse).
 * [StoreReadResponseOrigin.Fetcher] is a data class with optional `name: String?`,
 * while SourceOfTruth, Cache, and Initial are singleton objects.
 */
internal fun StoreReadResponseOrigin.toDataOrigin(): DataOrigin {
    return when (this) {
        is StoreReadResponseOrigin.Fetcher -> DataOrigin.NETWORK
        StoreReadResponseOrigin.SourceOfTruth -> DataOrigin.CACHE
        StoreReadResponseOrigin.Cache -> DataOrigin.MEMORY
        StoreReadResponseOrigin.Initial -> DataOrigin.MEMORY
    }
}

/**
 * Extracts a [Throwable] from any [StoreReadResponse.Error] variant.
 */
internal fun StoreReadResponse.Error.toThrowable(): Throwable {
    return when (this) {
        is StoreReadResponse.Error.Exception -> error
        is StoreReadResponse.Error.Message -> RuntimeException(message)
        is StoreReadResponse.Error.Custom<*> -> RuntimeException("Store error: $error")
    }
}
