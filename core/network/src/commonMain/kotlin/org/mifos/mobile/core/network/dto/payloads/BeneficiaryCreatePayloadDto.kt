package org.mifos.mobile.core.network.dto.payloads

import kotlinx.serialization.Serializable

@Serializable
data class BeneficiaryCreatePayloadDto(
    val locale: String? = null,

    val name: String? = null,

    val accountNumber: String? = null,

    val accountType: Int? = 0,

    val transferLimit: Int? = 0,

    val officeName: String? = null,
)