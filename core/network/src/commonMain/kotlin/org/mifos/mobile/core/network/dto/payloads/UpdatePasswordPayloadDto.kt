package org.mifos.mobile.core.network.dto.payloads

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePasswordPayloadDto(
    val password: String? = null,
    val repeatPassword: String? = null,
)
