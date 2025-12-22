package org.mifos.mobile.core.common.paging

data class PageResult<Key, Value>(
    val items: List<Value>,
    val prevKey: Key?,
    val nextKey: Key?
)
