package org.mifos.mobile.core.network.dto.payloads

import kotlinx.serialization.Serializable

@Serializable
data class UserVerifyPayloadDto(
    val requestId: String? = null,
    val authenticationToken: String? = null,
)
