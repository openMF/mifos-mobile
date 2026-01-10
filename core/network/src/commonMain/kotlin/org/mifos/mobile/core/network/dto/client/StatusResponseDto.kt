package org.mifos.mobile.core.network.dto.client

import kotlinx.serialization.Serializable

@Serializable
data class StatusResponseDto(
    val id: Int? = null,
    val code: String? = null,
    val value: String? = null,
)