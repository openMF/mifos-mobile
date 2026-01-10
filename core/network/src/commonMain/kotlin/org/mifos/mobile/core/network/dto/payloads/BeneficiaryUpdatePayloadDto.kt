package org.mifos.mobile.core.network.dto.payloads

import kotlinx.serialization.Serializable

@Serializable
data class BeneficiaryUpdatePayloadDto(
    val name: String? = null,
    val transferLimit: Int = 0,
)