package org.mifos.mobile.core.network.dto.payloads

import kotlinx.serialization.Serializable

@Serializable
data class ShareApplicationPayloadDto(
    val productId: Int,
    val unitPrice: Double,
    val requestedShares: Int,
    val submittedDate: String,
    val savingsAccountId: Int,
    val applicationDate: String,
    val locale: String,
    val dateFormat: String,
    val clientId: Long,
)
