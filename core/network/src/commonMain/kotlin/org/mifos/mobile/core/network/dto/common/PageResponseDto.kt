package org.mifos.mobile.core.network.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class PageResponseDto<T>(

    val totalFilteredRecords: Int = 0,
    val pageItems: List<T> = emptyList(),
)
