package org.mifos.mobile.core.network.dto.intrest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto

@Serializable
data class InterestRecalculationDataResponseDto(
    val id: Int? = null,

    val loanId: Int? = null,

    val interestRecalculationCompoundingType: TypeResponseDto? = null,

    val rescheduleStrategyType: TypeResponseDto? = null,

    val calendarData: CalendarDataResponseDto,

    val recalculationRestFrequencyType: TypeResponseDto? = null,

    val recalculationRestFrequencyInterval: Double? = null,

    val recalculationCompoundingFrequencyType: TypeResponseDto? = null,

    @SerialName("isCompoundingToBePostedAsTransaction")
    val compoundingToBePostedAsTransaction: Boolean? = null,

    val allowCompoundingOnEod: Boolean? = null,

)
