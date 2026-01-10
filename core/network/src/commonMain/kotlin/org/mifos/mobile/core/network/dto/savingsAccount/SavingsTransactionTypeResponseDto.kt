package org.mifos.mobile.core.network.dto.savingsAccount

import kotlinx.serialization.Serializable


@Serializable
data class SavingsTransactionTypeResponseDto(
    val id: Int? = null,
    val code: String? = null,

    val value: String? = null,

    val deposit: Boolean? = null,

    val dividendPayout: Boolean? = null,

    val withdrawal: Boolean? = null,

    val interestPosting: Boolean? = null,

    val feeDeduction: Boolean? = null,

    val initiateTransfer: Boolean? = null,

    val approveTransfer: Boolean? = null,

    val withdrawTransfer: Boolean? = null,

    val rejectTransfer: Boolean? = null,

    val overdraftInterest: Boolean? = null,

    val writtenoff: Boolean? = null,

    val overdraftFee: Boolean? = null,

    val withholdTax: Boolean? = null,

    val escheat: Boolean? = null,

)
