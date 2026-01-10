package org.mifos.mobile.core.network.dto.products.savings

import kotlinx.serialization.Serializable

@Serializable
data class SavingsProductResponseDto(
    val id: Int,
    val name: String,
    val withdrawalFeeForTransfers: Boolean = false,
    val allowOverdraft: Boolean = false,
    val enforceMinRequiredBalance: Boolean = false,
    val lienAllowed: Boolean = false,
    val withHoldTax: Boolean = false,
)