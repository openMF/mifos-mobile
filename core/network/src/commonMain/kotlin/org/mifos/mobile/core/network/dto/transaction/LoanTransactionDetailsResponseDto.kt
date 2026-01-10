package org.mifos.mobile.core.network.dto.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto


@Serializable
data class LoanTransactionDetailsResponseDto(
    val id: Long? = null,
    val amount: Double? = null,
    val date: List<Int>? = null,
    val manuallyReversed: Boolean? = null,
    val principalPortion: Double? = null,
    val outstandingLoanBalance: Double? = null,
    val interestPortion: Double? = null,
    val feeChargesPortion: Double? = null,
    val penaltyChargesPortion: Double? = null,
    val currency: CurrencyResponseDto? = null,
    @SerialName("type")
    val loanType: LoanTransactionTypeResponseDto? = null,
)


@Serializable
data class LoanTransactionTypeResponseDto(
    val value: String? = null,
    val code: String? = null,
    val disbursement: Boolean = false,
    val repayment: Boolean = false,
    val waiveCharges: Boolean = false,
)
