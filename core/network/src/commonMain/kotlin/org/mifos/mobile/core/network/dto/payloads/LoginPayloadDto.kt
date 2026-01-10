package org.mifos.mobile.core.network.dto.payloads

import kotlinx.serialization.Serializable

@Serializable
data class LoginPayloadDto(
    val username: String,
    val password: String,
)