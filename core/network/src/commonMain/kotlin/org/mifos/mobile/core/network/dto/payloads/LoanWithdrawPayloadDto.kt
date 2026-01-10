package org.mifos.mobile.core.network.dto.payloads

import kotlinx.serialization.Serializable

@Serializable
data class LoanWithdrawPayloadDto(
    val withdrawnOnDate: String? = null,
    val note: String? = null,
    val dateFormat: String? = null,
    val locale: String? = null,
)
