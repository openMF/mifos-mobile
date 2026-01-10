package org.mifos.mobile.core.network.dto.client

import kotlinx.serialization.Serializable

@Serializable
data class ClientTypeResponseDto(
    val id: Int,
    val name: String? = null,
    val active: Boolean,
    val mandatory: Boolean,
)