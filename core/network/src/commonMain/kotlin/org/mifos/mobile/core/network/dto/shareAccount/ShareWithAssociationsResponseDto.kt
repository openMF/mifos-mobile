package org.mifos.mobile.core.network.dto.shareAccount

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.charges.ChargeResponseDto
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.transaction.ShareTransactionResponseDto

@Serializable
data class ShareWithAssociationsResponseDto(
    val id: Long? = null,
    val accountNo: String? = null,
    val clientId: Long? = null,
    val clientName: String? = null,

    val productId: Long? = null,
    val productName: String? = null,

    val status: ShareStatusResponseDto? = null,
    val currency: CurrencyResponseDto? = null,
    val timeline: ShareTimelineResponseDto? = null,
    val summary: ShareSummaryResponseDto? = null,

    val currentMarketPrice: Double? = null,
    val savingsAccountId: Long? = null,
    val savingsAccountNumber: String? = null,
    val allowDividendCalculationForInactiveClients: Boolean? = null,

    val lockinPeriod: Int? = null,
    val lockPeriodTypeEnum: TypeResponseDto? = null,
    val minimumActivePeriod: Int? = null,
    val minimumActivePeriodTypeEnum: TypeResponseDto? = null,

    val charges: List<ChargeResponseDto> = emptyList(),
    val purchasedShares: List<ShareTransactionResponseDto> = emptyList(),
    val dividends: List<String> = emptyList(),
)
