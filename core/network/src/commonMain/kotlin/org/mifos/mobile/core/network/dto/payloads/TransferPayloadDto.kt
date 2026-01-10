package org.mifos.mobile.core.network.dto.payloads

import kotlinx.serialization.Serializable


@Serializable
data class TransferPayloadDto(
    val fromOfficeId: Int? = null,

    val fromClientId: Long? = null,

    val fromAccountType: Int? = null,

    val fromAccountId: String? = null,

    val toOfficeId: Int? = null,

    val toClientId: Long? = null,

    val toAccountType: Int? = null,

    val toAccountId: String? = null,

    val transferDate: String? = null,

    val transferAmount: Double? = null,

    val transferDescription: String? = null,

    val dateFormat: String? = null,

    val locale: String? = null,

)
