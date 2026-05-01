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

import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse

/**
 * Key for paginated Store requests.
 *
 * Use this as the Store key type when the data source supports pagination.
 * The Store will cache each page independently.
 *
 * @param page Zero-based page index.
 * @param pageSize Number of items per page.
 * @param query Optional search/filter query.
 */
data class PageKey(
    val page: Int,
    val pageSize: Int = DEFAULT_PAGE_SIZE,
    val query: String? = null,
) {
    companion object {
        const val DEFAULT_PAGE_SIZE = 20

        /** First page with default size. */
        fun first(pageSize: Int = DEFAULT_PAGE_SIZE, query: String? = null): PageKey =
            PageKey(page = 0, pageSize = pageSize, query = query)
    }

    /** Next page key. */
    fun next(): PageKey = copy(page = page + 1)

    /** Offset for SQL LIMIT/OFFSET queries. */
    val offset: Int get() = page * pageSize
}

/**
 * Triggers a Store refresh for a specific page and suspends until data arrives.
 *
 * Use this inside a PagingSource.load() implementation to let Store handle
 * caching while Paging handles the pagination UX.
 *
 * Usage in core/domain (where paging transformation lives):
 * ```
 * class ClientPagingSource(
 *     private val store: Store<PageKey, List<Client>>,
 * ) : PagingSource<Int, Client>() {
 *
 *     override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Client> {
 *         val page = params.key ?: 0
 *         val pageKey = PageKey(page = page, pageSize = params.loadSize)
 *         return store.loadPage(pageKey)
 *     }
 * }
 * ```
 *
 * **Important:** Store's `stream()` returns an infinite Flow (never completes).
 * This function uses `filterNot` + `first` to get the first Data or Error emission
 * and then cancel the flow. Do NOT use `collect { return@collect }` -- `return@collect`
 * only returns from the lambda, it does not cancel the flow.
 *
 * @param key The page key to load.
 * @return Success with items and pagination keys, or Error.
 */
suspend fun <Value : Any> Store<PageKey, List<Value>>.loadPage(
    key: PageKey,
): StorePageResult<Value> {
    val response = stream(StoreReadRequest.cached(key, refresh = true))
        .filterNot {
            it is StoreReadResponse.Loading ||
                it is StoreReadResponse.NoNewData ||
                it is StoreReadResponse.Initial
        }
        .first()

    return when (response) {
        is StoreReadResponse.Data -> {
            val items = response.value
            StorePageResult.Success(
                items = items,
                prevKey = if (key.page > 0) key.page - 1 else null,
                nextKey = if (items.size >= key.pageSize) key.page + 1 else null,
            )
        }

        is StoreReadResponse.Error -> StorePageResult.Error(response.toThrowable())

        else -> StorePageResult.Error(RuntimeException("Unexpected store response: $response"))
    }
}

/**
 * Result of a paginated Store load, matching PagingSource.LoadResult shape.
 *
 * Consumer apps map this to `PagingSource.LoadResult` in their domain layer:
 * ```
 * when (result) {
 *     is StorePageResult.Success -> LoadResult.Page(result.items, result.prevKey, result.nextKey)
 *     is StorePageResult.Error -> LoadResult.Error(result.error)
 * }
 * ```
 */
sealed class StorePageResult<out T> {
    data class Success<T>(
        val items: List<T>,
        val prevKey: Int?,
        val nextKey: Int?,
    ) : StorePageResult<T>()

    data class Error(val error: Throwable) : StorePageResult<Nothing>()
}
