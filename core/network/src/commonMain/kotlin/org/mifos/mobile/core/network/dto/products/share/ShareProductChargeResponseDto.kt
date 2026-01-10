package org.mifos.mobile.core.network.dto.products.share

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto

@Serializable
data class ShareProductChargeResponseDto(
    val id: Int? = null,
    val name: String? = null,
    val active: Boolean? = null,
    val penalty: Boolean? = null,
    val currency: CurrencyResponseDto? = null,
    val amount: Double? = null,
    val chargeTimeType: TypeResponseDto? = null,
    val chargeAppliesTo: TypeResponseDto? = null,
    val chargeCalculationType: TypeResponseDto? = null,
    val chargePaymentMode: TypeResponseDto? = null,
)
