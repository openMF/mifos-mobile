package org.mifos.mobile.core.network.dto.loanAccount

import kotlinx.serialization.Serializable

@Serializable
data class LoanCurrencyResponseDto(
    val code: String? = null,

    val name: String? = null,

    val decimalPlaces: Double? = null,

    val inMultiplesOf: Double? = null,

    val displaySymbol: String? = null,

    val nameCode: String? = null,

    val displayLabel: String? = null,
)