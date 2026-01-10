package org.mifos.mobile.core.network.dto.transaction

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto

@Serializable
data class TransactionResponseDto(

    val id: Long? = null,

    val officeId: Long? = null,

    val officeName: String? = null,

    val type: TypeResponseDto,

    val date: List<Int> = emptyList(),

    val currency: CurrencyResponseDto? = null,

    val amount: Double? = null,

    val submittedOnDate: List<Int> = emptyList(),

    val reversed: Boolean? = null,

)
