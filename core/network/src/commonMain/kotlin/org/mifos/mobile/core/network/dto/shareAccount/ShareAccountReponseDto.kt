package org.mifos.mobile.core.network.dto.shareAccount

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto

@Serializable
data class ShareAccountResponseDto(

    val id: Long = 0,

    val accountNo: String? = null,

    val totalApprovedShares: Int? = null,

    val totalPendingForApprovalShares: Int? = null,

    val productId: Int? = null,

    val productName: String? = null,

    val shortProductName: String? = null,

    val status: ShareStatusResponseDto? = null,

    val currency: CurrencyResponseDto? = null,

    val timeline: ShareTimelineResponseDto? = null,

)


