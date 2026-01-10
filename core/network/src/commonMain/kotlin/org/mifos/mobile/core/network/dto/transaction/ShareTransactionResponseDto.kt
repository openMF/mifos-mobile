package org.mifos.mobile.core.network.dto.transaction

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto

@Serializable
data class ShareTransactionResponseDto(
    val accountId: Long? = null,
    val amount: Double? = null,
    val amountPaid: Double? = null,
    val chargeAmount: Double? = null,
    val id: Long? = null,
    val numberOfShares: Int? = null,
    val purchasedDate: List<Int> = emptyList(),
    val purchasedPrice: Double? = null,
    val status: TypeResponseDto? = null,
    val type: TypeResponseDto? = null,
)
