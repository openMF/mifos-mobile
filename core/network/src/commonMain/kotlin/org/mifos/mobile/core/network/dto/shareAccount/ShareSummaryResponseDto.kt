package org.mifos.mobile.core.network.dto.shareAccount

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto


@Serializable
data class ShareSummaryResponseDto(
    val id: Long? = null,
    val accountNo: String? = null,
    val productId: Long? = null,
    val productName: String? = null,
    val status: ShareStatusResponseDto? = null,
    val currency: CurrencyResponseDto? = null,
    val timeline: ShareTimelineResponseDto? = null,
    val totalApprovedShares: Int? = null,
    val totalPendingForApprovalShares: Int? = null,
)
