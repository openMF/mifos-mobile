package org.mifos.mobile.core.network.dto.charges

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto


@Serializable
data class ChargeResponseDto(
    val clientId: Int? = null,
    val chargeId: Int? = null,
    val name: String? = null,
    val dueDate: ArrayList<Int?> = arrayListOf(),
    val chargeTimeType: TypeResponseDto? = null,
    val chargeCalculationType: TypeResponseDto? = null,
    val currency: CurrencyResponseDto? = null,
    val amount: Double = 0.0,
    val amountPaid: Double = 0.0,
    val amountWaived: Double = 0.0,
    val amountWrittenOff: Double = 0.0,
    val amountOutstanding: Double = 0.0,
    val penalty: Boolean = false,
    val isActive: Boolean = false,
    val isChargePaid: Boolean = false,
    val isChargeWaived: Boolean = false,
    val paid: Boolean = false,
    val waived: Boolean = false,
)
