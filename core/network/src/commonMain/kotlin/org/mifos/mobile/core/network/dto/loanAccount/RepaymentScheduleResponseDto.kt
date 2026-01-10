package org.mifos.mobile.core.network.dto.loanAccount

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto


@Serializable
data class RepaymentScheduleResponseDto(
    val currency: CurrencyResponseDto? = null,

    val loanTermInDays: Int? = null,

    val totalPrincipalDisbursed: Double? = null,

    val totalPrincipalExpected: Double? = null,

    val totalPrincipalPaid: Double? = null,

    val totalInterestCharged: Double? = null,

    val totalFeeChargesCharged: Double? = null,

    val totalPenaltyChargesCharged: Double? = null,

    val totalWaived: Double? = null,

    val totalWrittenOff: Double? = null,

    val totalRepaymentExpected: Double? = null,

    val totalRepayment: Double? = null,

    val totalPaidInAdvance: Double? = null,

    val totalPaidLate: Double? = null,

    val totalOutstanding: Double? = null,

    val periods: List<PeriodsResponseDto> = emptyList(),
)
