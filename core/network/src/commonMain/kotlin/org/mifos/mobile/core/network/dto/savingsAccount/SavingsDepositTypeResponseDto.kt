package org.mifos.mobile.core.network.dto.savingsAccount

import kotlinx.serialization.Serializable

@Serializable
data class SavingsDepositTypeResponseDto(
    val id: Int? = null,
    val code: String? = null,
    val value: String? = null,
)