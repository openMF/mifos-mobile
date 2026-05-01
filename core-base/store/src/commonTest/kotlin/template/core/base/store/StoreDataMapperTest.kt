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

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin
import template.core.base.common.DataState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

// --- Network + Cache mode tests ---

class StoreDataMapperCachedTest {

    @Test
    fun cacheDataEmitsWithCacheOrigin() = runTest {
        val flow = flowOf(
            StoreReadResponse.Data("cached", StoreReadResponseOrigin.SourceOfTruth),
        )
        flow.mapToStoreData().test {
            val item = awaitItem()
            assertEquals("cached", item.data)
            assertEquals(DataOrigin.CACHE, item.origin)
            assertFalse(item.isRefreshing)
            assertTrue(item.isStale)
            awaitComplete()
        }
    }

    @Test
    fun loadingThenCacheSetsRefreshing() = runTest {
        val flow = flowOf(
            StoreReadResponse.Loading(StoreReadResponseOrigin.Fetcher()),
            StoreReadResponse.Data("cached", StoreReadResponseOrigin.SourceOfTruth),
        )
        flow.mapToStoreData().test {
            val item = awaitItem()
            assertEquals("cached", item.data)
            assertTrue(item.isRefreshing)
            assertEquals(DataOrigin.CACHE, item.origin)
            awaitComplete()
        }
    }

    @Test
    fun cacheThenNetworkSequence() = runTest {
        val flow = flowOf(
            StoreReadResponse.Loading(StoreReadResponseOrigin.Fetcher()),
            StoreReadResponse.Data("cached", StoreReadResponseOrigin.SourceOfTruth),
            StoreReadResponse.Data("fresh", StoreReadResponseOrigin.Fetcher()),
        )
        flow.mapToStoreData().test {
            val cached = awaitItem()
            assertEquals("cached", cached.data)
            assertTrue(cached.isRefreshing)
            assertTrue(cached.isStale)

            val fresh = awaitItem()
            assertEquals("fresh", fresh.data)
            assertFalse(fresh.isRefreshing)
            assertFalse(fresh.isStale)
            assertNotNull(fresh.fetchedAt)
            assertEquals(DataOrigin.NETWORK, fresh.origin)
            awaitComplete()
        }
    }

    @Test
    fun errorAfterCacheKeepsLastData() = runTest {
        val flow = flowOf(
            StoreReadResponse.Data("cached", StoreReadResponseOrigin.SourceOfTruth),
            StoreReadResponse.Error.Exception(
                error = RuntimeException("timeout"),
                origin = StoreReadResponseOrigin.Fetcher(),
            ),
        )
        flow.mapToStoreDataWithErrors(fallback = "unused").test {
            val cached = awaitItem()
            assertEquals("cached", cached.data)
            assertNull(cached.error)
            assertTrue(cached.isSuccess)

            val errored = awaitItem()
            assertEquals("cached", errored.data)
            assertNotNull(errored.error)
            assertFalse(errored.isEmpty)
            assertFalse(errored.isError)
            awaitComplete()
        }
    }
}

// --- Network-only mode tests ---

class StoreDataMapperNetworkOnlyTest {

    @Test
    fun networkOnlySuccessEmitsWithNetworkOrigin() = runTest {
        val flow = flowOf(
            StoreReadResponse.Loading(StoreReadResponseOrigin.Fetcher()),
            StoreReadResponse.Data("fresh", StoreReadResponseOrigin.Fetcher()),
        )
        flow.mapToStoreData().test {
            val item = awaitItem()
            assertEquals("fresh", item.data)
            assertEquals(DataOrigin.NETWORK, item.origin)
            assertFalse(item.isRefreshing)
            assertFalse(item.isStale)
            assertNotNull(item.fetchedAt)
            awaitComplete()
        }
    }

    @Test
    fun networkOnlyErrorBeforeAnyData() = runTest {
        val flow = flowOf(
            StoreReadResponse.Loading(StoreReadResponseOrigin.Fetcher()),
            StoreReadResponse.Error.Message(
                message = "Network unavailable",
                origin = StoreReadResponseOrigin.Fetcher(),
            ),
        )
        flow.mapToStoreDataWithErrors(fallback = "none").test {
            val item = awaitItem()
            assertEquals("none", item.data)
            assertTrue(item.isEmpty)
            assertTrue(item.isError)
            val error = assertNotNull(item.error)
            assertEquals("Network unavailable", error.message)
            awaitComplete()
        }
    }

    @Test
    fun networkOnlyEmptyResult() = runTest {
        val flow = flowOf(
            StoreReadResponse.Data(emptyList<String>(), StoreReadResponseOrigin.Fetcher()),
        )
        flow.mapToStoreData(isEmpty = { it.isEmpty() }).test {
            val item = awaitItem()
            assertTrue(item.isEmpty)
            assertTrue(item.data.isEmpty())
            assertEquals(DataOrigin.NETWORK, item.origin)
            awaitComplete()
        }
    }
}

// --- Shared behavior tests ---

class StoreDataMapperSharedTest {

    @Test
    fun initialResponseProducesNoEmissions() = runTest {
        flowOf(StoreReadResponse.Initial)
            .mapToStoreData().test { awaitComplete() }
    }

    @Test
    fun loadingAloneProducesNoEmissions() = runTest {
        flowOf(StoreReadResponse.Loading(StoreReadResponseOrigin.Fetcher()))
            .mapToStoreData().test { awaitComplete() }
    }

    @Test
    fun noNewDataProducesNoEmissions() = runTest {
        flowOf(StoreReadResponse.NoNewData(StoreReadResponseOrigin.Fetcher()))
            .mapToStoreData().test { awaitComplete() }
    }

    @Test
    fun memoryOriginMapsCorrectly() = runTest {
        flowOf(StoreReadResponse.Data("memory", StoreReadResponseOrigin.Cache))
            .mapToStoreData().test {
                assertEquals(DataOrigin.MEMORY, awaitItem().origin)
                awaitComplete()
            }
    }
}

// --- StoreData property tests ---

class StoreDataTest {

    @Test
    fun isStaleWhenNeverFetched() {
        val data = StoreData("test", DataOrigin.CACHE, fetchedAt = null)
        assertTrue(data.isStale)
        assertNull(data.staleDuration)
    }

    @Test
    fun notStaleWhenFetched() {
        val mark = kotlin.time.TimeSource.Monotonic.markNow()
        val data = StoreData("test", DataOrigin.NETWORK, fetchedAt = mark)
        assertFalse(data.isStale)
        assertNotNull(data.staleDuration)
    }

    @Test
    fun networkOriginNeverStale() {
        val data = StoreData("test", DataOrigin.NETWORK, fetchedAt = null)
        assertFalse(data.isStale)
    }

    @Test
    fun isSuccessWhenNoErrorAndNotEmpty() {
        val data = StoreData("test", DataOrigin.NETWORK)
        assertTrue(data.isSuccess)
        assertFalse(data.isError)
    }

    @Test
    fun isErrorWhenErrorAndEmpty() {
        val data = StoreData(
            "",
            DataOrigin.NETWORK,
            error = RuntimeException("fail"),
            isEmpty = true,
        )
        assertTrue(data.isError)
        assertFalse(data.isSuccess)
    }

    @Test
    fun mapPreservesMetadata() {
        val mark = kotlin.time.TimeSource.Monotonic.markNow()
        val original = StoreData(
            "42",
            DataOrigin.NETWORK,
            isRefreshing = true,
            fetchedAt = mark,
        )
        val mapped = original.map { it.toInt() }
        assertEquals(42, mapped.data)
        assertEquals(DataOrigin.NETWORK, mapped.origin)
        assertTrue(mapped.isRefreshing)
        assertEquals(mark, mapped.fetchedAt)
    }
}

// --- DataState bridge tests ---

class StoreDataBridgeTest {

    @Test
    fun toDataStateSuccess() {
        val data = StoreData("test", DataOrigin.NETWORK)
        assertTrue(data.toDataState() is DataState.Success)
    }

    @Test
    fun toDataStatePendingWhenRefreshing() {
        val data = StoreData("cached", DataOrigin.CACHE, isRefreshing = true)
        val state = data.toDataState()
        assertTrue(state is DataState.Pending)
        assertEquals("cached", state.data)
    }

    @Test
    fun toDataStateErrorWithData() {
        val data = StoreData("stale", DataOrigin.CACHE, error = RuntimeException("fail"))
        val state = data.toDataState()
        assertTrue(state is DataState.Error)
        assertEquals("stale", state.data)
    }

    @Test
    fun toDataStateLoadingWhenEmpty() {
        val data = StoreData("", DataOrigin.NETWORK, isEmpty = true)
        assertTrue(data.toDataState() is DataState.Loading)
    }

    @Test
    fun toDataStateErrorWithNoData() {
        val data = StoreData(
            "",
            DataOrigin.NETWORK,
            error = RuntimeException("fail"),
            isEmpty = true,
        )
        val state = data.toDataState()
        assertTrue(state is DataState.Error)
        assertNull(state.data)
    }
}
