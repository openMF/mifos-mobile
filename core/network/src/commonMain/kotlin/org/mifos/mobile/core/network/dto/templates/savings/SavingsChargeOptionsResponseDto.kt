package org.mifos.mobile.core.network.dto.templates.savings

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto


@Serializable
data class SavingsChargeOptionsResponseDto(
    val id: Int? = null,
    val name: String? = null,
    val active: Boolean? = null,
    val penalty: Boolean? = null,
    val currency: CurrencyResponseDto? = null,
    val amount: Float = 0f,
    val chargeTimeType: TypeResponseDto,
    val chargeAppliesTo: TypeResponseDto,
    val chargeCalculationType: TypeResponseDto,
    val chargePaymentMode: TypeResponseDto,
)
