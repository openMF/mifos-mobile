package org.mifos.mobile.core.network.dto.transaction

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsTransactionTypeResponseDto

@Serializable
data class SavingsTransactionResponseDto(
    val id: Int? = null,

    val transactionType: SavingsTransactionTypeResponseDto? = null,

    val accountId: Int? = null,

    val accountNo: String? = null,

    val date: List<Int> = emptyList(),

    val currency: CurrencyResponseDto? = null,

    val paymentDetailData: PaymentDetailsResponseDto? = null,

    val amount: Double? = null,

    val runningBalance: Double? = null,

    val reversed: Boolean? = null,

    val submittedOnDate: List<Int>? = null,

    val interestedPostedAsOn: Boolean? = null,

)
