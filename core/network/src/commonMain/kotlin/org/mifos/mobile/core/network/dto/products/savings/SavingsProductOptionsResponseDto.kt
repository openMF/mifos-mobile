package org.mifos.mobile.core.network.dto.products.savings

import kotlinx.serialization.Serializable

@Serializable
data class SavingsProductOptionsResponseDto(
    val id: Int? = null,
    val name: String? = null,
)