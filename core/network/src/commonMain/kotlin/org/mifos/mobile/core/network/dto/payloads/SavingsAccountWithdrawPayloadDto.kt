package org.mifos.mobile.core.network.dto.payloads

import kotlinx.serialization.Serializable

@Serializable
data class SavingsAccountWithdrawPayloadDto(
    val locale: String? = null,
    val dateFormat: String? = null,
    val withdrawnOnDate: String? = null,
    val note: String? = null,
)