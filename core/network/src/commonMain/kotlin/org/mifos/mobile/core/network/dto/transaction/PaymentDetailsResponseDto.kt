package org.mifos.mobile.core.network.dto.transaction

import kotlinx.serialization.Serializable

@Serializable
data class PaymentDetailsResponseDto(
    val id: Int? = null,

    val paymentType: PaymentTypeResponseDto,

    val accountNumber: String? = null,

    val checkNumber: String? = null,

    val routingCode: String? = null,

    val receiptNumber: String? = null,

    val bankNumber: String? = null,
)

@Serializable
data class PaymentTypeResponseDto(
    val id: Int? = null,

    val name: String? = null,
)
