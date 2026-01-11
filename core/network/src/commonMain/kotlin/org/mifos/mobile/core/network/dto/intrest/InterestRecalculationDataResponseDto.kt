/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
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
