package org.mifos.mobile.core.network.dto.payloads

import kotlinx.serialization.Serializable

@Serializable
data class SavingsAccountUpdatePayloadDto(

    val clientId: Long? = 0,

    val productId: Long? = 0,

)
