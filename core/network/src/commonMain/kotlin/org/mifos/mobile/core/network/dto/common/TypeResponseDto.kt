package org.mifos.mobile.core.network.dto.common

import kotlinx.serialization.Serializable


@Serializable
data class TypeResponseDto(
    val id: Int? = null,
    val code: String? = null,

    val value: String? = null,
)
