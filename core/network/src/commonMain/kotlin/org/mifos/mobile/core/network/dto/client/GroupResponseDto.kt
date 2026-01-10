package org.mifos.mobile.core.network.dto.client

import kotlinx.serialization.Serializable

@Serializable
data class GroupResponseDto(
    val id: Int,
    val accountNo: String? = null,
    val name: String? = null,
)