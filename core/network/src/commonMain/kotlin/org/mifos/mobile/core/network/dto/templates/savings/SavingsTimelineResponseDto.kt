package org.mifos.mobile.core.network.dto.templates.savings

import kotlinx.serialization.Serializable

@Serializable
data class SavingsTimelineResponseDto(
    val expectedDisbursementDate: List<Int> = emptyList(),
)
