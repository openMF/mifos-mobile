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
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreReadRequest

/**
 * Streams data from a [Store] with full [StoreData] metadata.
 *
 * This is the primary API for repositories. Source mode (network+cache vs network-only)
 * is determined by how the Store was created:
 * - [StoreFactory.createStore] -> network + cache (Fetcher + SourceOfTruth)
 * - [StoreFactory.createMemoryStore] -> network only (Fetcher only, in-memory cache)
 *
 * Both produce the same `Flow<StoreData<Output>>` -- the ViewModel doesn't need to know.
 *
 * @param key The data identifier.
 * @param refresh Whether to trigger a network refresh. Default true.
 * @param isEmpty Predicate to detect empty results.
 */
fun <Key : Any, Output : Any> Store<Key, Output>.streamData(
    key: Key,
    refresh: Boolean = true,
    isEmpty: (Output) -> Boolean = { false },
): Flow<StoreData<Output>> {
    return stream(StoreReadRequest.cached(key, refresh))
        .mapToStoreData(isEmpty)
}

/**
 * Like [streamData] but also emits on errors with last known data preserved.
 *
 * @param key The data identifier.
 * @param fallback Value to use if error arrives before any data.
 * @param refresh Whether to trigger a network refresh. Default true.
 * @param isEmpty Predicate to detect empty results.
 */
fun <Key : Any, Output : Any> Store<Key, Output>.streamDataWithErrors(
    key: Key,
    fallback: Output,
    refresh: Boolean = true,
    isEmpty: (Output) -> Boolean = { false },
): Flow<StoreData<Output>> {
    return stream(StoreReadRequest.cached(key, refresh))
        .mapToStoreDataWithErrors(fallback, isEmpty)
}

/**
 * Forces a fresh network fetch, ignoring any cached data.
 * Useful for pull-to-refresh or explicit "reload" actions.
 */
fun <Key : Any, Output : Any> Store<Key, Output>.freshData(
    key: Key,
    isEmpty: (Output) -> Boolean = { false },
): Flow<StoreData<Output>> {
    return stream(StoreReadRequest.fresh(key, fallBackToSourceOfTruth = true))
        .mapToStoreData(isEmpty)
}

/**
 * Reads only from local cache/database, no network.
 * Useful for offline-only screens or pre-fetched data.
 */
fun <Key : Any, Output : Any> Store<Key, Output>.localData(
    key: Key,
    isEmpty: (Output) -> Boolean = { false },
): Flow<StoreData<Output>> {
    return stream(StoreReadRequest.localOnly(key))
        .mapToStoreData(isEmpty)
}

/**
 * Maps [StoreData] content while preserving all metadata.
 */
fun <T, R> StoreData<T>.map(transform: (T) -> R): StoreData<R> {
    return StoreData(
        data = transform(data),
        origin = origin,
        isRefreshing = isRefreshing,
        fetchedAt = fetchedAt,
        error = error,
        isEmpty = isEmpty,
    )
}

/**
 * Maps a Flow of [StoreData] content while preserving all metadata.
 */
fun <T, R> Flow<StoreData<T>>.mapData(transform: (T) -> R): Flow<StoreData<R>> {
    return map { it.map(transform) }
}
