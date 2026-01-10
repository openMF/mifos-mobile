package org.mifos.mobile.core.network.dto.savingsAccount

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto

@Serializable
data class SavingsSummaryResponseDto(

    val currency: CurrencyResponseDto? = null,

    val totalDeposits: Double? = null,

    val totalWithdrawals: Double? = null,

    val totalInterestEarned: Double? = null,

    val totalInterestPosted: Double? = null,

    val accountBalance: Double? = null,

    val totalOverdraftInterestDerived: Double? = null,

    val interestNotPosted: Double? = null,

    val lastInterestCalculationDate: List<Int> = emptyList(),
)
