package org.mifos.mobile.core.network.dto.currency

import kotlinx.serialization.Serializable


@Serializable
data class CurrencyResponseDto(
    val code: String? = null,
    val name: String? = null,
    val decimalPlaces: Int = 0,
    val inMultiplesOf: Double = 0.0,
    val displaySymbol: String? = null,
    val nameCode: String? = null,
    val displayLabel: String? = null,
)
