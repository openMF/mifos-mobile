package org.mifos.mobile.core.common.paging

typealias PageFetcher<Key, Value> = suspend (
    key: Key?,
    loadSize: Int
) -> PageResult<Key, Value>
