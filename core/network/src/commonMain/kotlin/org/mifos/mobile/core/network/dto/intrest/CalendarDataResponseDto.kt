package org.mifos.mobile.core.network.dto.intrest

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto


@Serializable
data class CalendarDataResponseDto(
    val id: Int? = null,

    val calendarInstanceId: Int? = null,

    val entityId: Int? = null,

    val entityType: TypeResponseDto,

    val title: String? = null,

    val startDate: List<Int> = emptyList(),

    val endDate: List<Int> = emptyList(),

    val duration: Double? = null,

    val type: TypeResponseDto,

    val repeating: Boolean? = null,

    val recurrence: String? = null,

    val frequency: TypeResponseDto,

    val interval: Double? = null,

    val repeatsOnNthDayOfMonth: TypeResponseDto,

    val firstReminder: Int? = null,

    val secondReminder: Int? = null,

    val humanReadable: String? = null,

    val createdDate: String? = null,

    val lastUpdatedDate: String? = null,

    val createdByUserId: Int? = null,

    val createdByUsername: String? = null,

    val lastUpdatedByUserId: Int? = null,

    val lastUpdatedByUsername: String? = null,
)
