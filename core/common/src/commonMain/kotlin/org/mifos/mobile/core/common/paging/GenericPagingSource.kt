package org.mifos.mobile.core.common.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

class GenericPagingSource<Key : Any, Value : Any>(
    private val fetcher: PageFetcher<Key, Value>
) : PagingSource<Key, Value>() {

    override suspend fun load(
        params: LoadParams<Key>
    ): LoadResult<Key, Value> {
        return  try {
            val result = fetcher(params.key, params.loadSize)

            LoadResult.Page(
                data = result.items,
                prevKey = result.prevKey,
                nextKey = result.nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<Key, Value>
    ): Key? {
        return state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.nextKey
                ?: state.closestPageToPosition(pos)?.prevKey
        }
    }
}
