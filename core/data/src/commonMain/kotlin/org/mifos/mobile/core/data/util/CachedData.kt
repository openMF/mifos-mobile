/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mifos.mobile.core.common.DataState
import template.core.base.store.DataOrigin
import template.core.base.store.StoreData

/**
 * Bridges Store5 [StoreData] to mifos [DataState] with freshness metadata.
 *
 * Preserves staleness information for UI indicators without requiring
 * core/ui to depend on Store5 types.
 *
 * @param data The actual data payload.
 * @param isFromCache True if data was served from local cache.
 * @param isRefreshing True when network fetch is in progress.
 * @param lastFetchedMs Epoch millis of last successful network fetch, or null.
 * @param error Non-null if refresh failed (stale data may still be usable).
 */
data class CachedData<out T>(
    val data: T,
    val isFromCache: Boolean,
    val isRefreshing: Boolean = false,
    val lastFetchedMs: Long? = null,
    val error: Throwable? = null,
)

/**
 * Converts [StoreData] to mifos [DataState], mapping:
 * - empty + no error → Loading
 * - error with no usable data → Error(null)
 * - error with stale data → Error(data) (UI can show stale + error banner)
 * - refreshing with cached data → Success(data) (UI shows refresh indicator separately)
 * - fresh data → Success(data)
 */
fun <T> StoreData<T>.toMifosDataState(): DataState<T> {
    return when {
        isEmpty && error == null -> DataState.Loading
        error != null -> DataState.Error(error!!, if (isEmpty) null else data)
        else -> DataState.Success(data)
    }
}

/**
 * Converts [StoreData] to [CachedData] preserving freshness metadata.
 * Use this when the ViewModel needs staleness info for UI indicators.
 */
fun <T> StoreData<T>.toCachedData(lastFetchedMs: Long? = null): CachedData<T> {
    return CachedData(
        data = data,
        isFromCache = origin == DataOrigin.CACHE,
        isRefreshing = isRefreshing,
        lastFetchedMs = lastFetchedMs,
        error = error,
    )
}

/**
 * Maps a Flow<StoreData<T>> to Flow<DataState<T>> using mifos DataState.
 */
fun <T> Flow<StoreData<T>>.asMifosDataStateFlow(): Flow<DataState<T>> =
    map { it.toMifosDataState() }
