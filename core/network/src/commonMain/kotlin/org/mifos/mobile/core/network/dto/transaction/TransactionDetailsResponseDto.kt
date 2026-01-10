package org.mifos.mobile.core.network.dto.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto


@Serializable
data class TransactionDetailsResponseDto(

    val id: Long? = null,

    val officeId: Long? = null,

    val officeName: String? = null,

    @SerialName("transactionType")
    val type: TypeResponseDto? = null,

    val date: List<Int> = emptyList(),

    val currency: CurrencyResponseDto? = null,

    val amount: Double? = null,

    val submittedOnDate: List<Int> = emptyList(),

    val reversed: Boolean? = null,

    val accountNo: String? = null,

    val manuallyReversed: Boolean? = null,

    val externalId: String? = null,

    val outstandingLoanBalance: Double? = null,

    val runningBalance: Double? = null,

    val principalPortion: Double? = null,

    val interestPortion: Double? = null,

    val feeChargesPortion: Double? = null,

    val penaltyChargesPortion: Double? = null,
)
