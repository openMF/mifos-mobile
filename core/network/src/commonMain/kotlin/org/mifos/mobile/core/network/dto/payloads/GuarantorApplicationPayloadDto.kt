package org.mifos.mobile.core.network.dto.payloads

import kotlinx.serialization.Serializable

@Serializable
data class GuarantorApplicationPayloadDto(

    val guarantorTypeId: Long? = null,

    val firstName: String? = null,

    val lastName: String? = null,

    val city: String? = "",
)