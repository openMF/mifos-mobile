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

import org.mobilenativefoundation.store.core5.ExperimentalStoreApi
import org.mobilenativefoundation.store.store5.Bookkeeper
import org.mobilenativefoundation.store.store5.Converter
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.MutableStore
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.Updater
import org.mobilenativefoundation.store.store5.Validator

/**
 * Factory for creating [Store] and [MutableStore] instances with sensible defaults.
 *
 * Wraps Store 5's builder API into two concise factory methods:
 * - [createStore] for read-only stores (network fetch + local cache)
 * - [createMutableStore] for read-write stores (adds write-back + offline sync)
 */
object StoreFactory {

    /**
     * Creates a read-only [Store] where the fetcher output type matches
     * the source of truth input type (no conversion needed).
     *
     * @param Key The type used to identify data (e.g., a user ID or query params).
     * @param Input The type produced by the fetcher and consumed by the source of truth.
     * @param Output The domain type exposed to consumers.
     * @param fetcher Network data source that fetches fresh data for a given key.
     * @param sourceOfTruth Local persistence layer (reader emits cached data, writer persists).
     * @param validator Optional cache validity check (e.g., TTL-based expiration).
     * @return A configured [Store] ready for streaming reads.
     */
    fun <Key : Any, Input : Any, Output : Any> createStore(
        fetcher: Fetcher<Key, Input>,
        sourceOfTruth: SourceOfTruth<Key, Input, Output>,
        validator: Validator<Output>? = null,
    ): Store<Key, Output> {
        var builder = StoreBuilder.from(
            fetcher = fetcher,
            sourceOfTruth = sourceOfTruth,
        )
        if (validator != null) {
            builder = builder.validator(validator)
        }
        return builder.build()
    }

    /**
     * Creates a read-only [Store] backed only by a [Fetcher] (no local persistence).
     *
     * Data is cached in-memory only. Useful for transient data that doesn't
     * need to survive process death.
     *
     * @param Key The type used to identify data.
     * @param Output The type produced by the fetcher and exposed to consumers.
     * @param fetcher Network data source.
     * @return A configured [Store] with in-memory caching only.
     */
    fun <Key : Any, Output : Any> createMemoryStore(
        fetcher: Fetcher<Key, Output>,
    ): Store<Key, Output> {
        return StoreBuilder.from(fetcher = fetcher).build()
    }

    /**
     * Creates a [MutableStore] that supports reads, writes, and offline sync.
     *
     * Uses a [Converter] to transform between network, local, and output types.
     * When a write fails (e.g., no network), the [Bookkeeper] records the failure
     * for retry on connectivity restore.
     *
     * @param Key The type used to identify data.
     * @param Network The raw type returned by the network layer.
     * @param Local The type persisted in local storage.
     * @param Output The domain type exposed to consumers.
     * @param fetcher Network data source for reads.
     * @param sourceOfTruth Local persistence layer.
     * @param converter Transforms between Network, Local, and Output types.
     * @param updater Writes data back to the server (e.g., POST/PUT API call).
     * @param bookkeeper Tracks unsynced local changes for retry on reconnection.
     * @param validator Optional cache validity check.
     * @return A configured [MutableStore] ready for reads and writes.
     */
    @OptIn(ExperimentalStoreApi::class)
    fun <Key : Any, Network : Any, Local : Any, Output : Any> createMutableStore(
        fetcher: Fetcher<Key, Network>,
        sourceOfTruth: SourceOfTruth<Key, Local, Output>,
        converter: Converter<Network, Local, Output>,
        updater: Updater<Key, Output, *>,
        bookkeeper: Bookkeeper<Key>,
        validator: Validator<Output>? = null,
    ): MutableStore<Key, Output> {
        var builder = StoreBuilder.from(
            fetcher = fetcher,
            sourceOfTruth = sourceOfTruth,
            converter = converter,
        )
        if (validator != null) {
            builder = builder.validator(validator)
        }
        return builder
            .toMutableStoreBuilder(converter)
            .build(
                updater = updater,
                bookkeeper = bookkeeper,
            )
    }
}
