package org.mifos.mobile.core.network.dto.loanAccount

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto

@Serializable
data class LoanSummaryResponseDto(
    val principalDisbursed: Double = 0.0,

    val principalPaid: Double = 0.0,

    val interestCharged: Double = 0.0,

    val interestPaid: Double = 0.0,

    val feeChargesCharged: Double = 0.0,

    val penaltyChargesCharged: Double = 0.0,

    val penaltyChargesWaived: Double = 0.0,

    val totalExpectedRepayment: Double = 0.0,

    val interestWaived: Double = 0.0,

    val totalRepayment: Double = 0.0,

    val feeChargesWaived: Double = 0.0,

    val totalOutstanding: Double = 0.0,

    private val overdueSinceDate: List<Int>? = null,

    val currency: CurrencyResponseDto? = null,
)