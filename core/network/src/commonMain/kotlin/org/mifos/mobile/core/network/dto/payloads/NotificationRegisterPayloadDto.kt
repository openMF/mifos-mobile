package org.mifos.mobile.core.network.dto.payloads

import kotlinx.serialization.Serializable

@Serializable
data class NotificationRegisterPayloadDto(
    val clientId: Long,
    val registrationId: String,
)
