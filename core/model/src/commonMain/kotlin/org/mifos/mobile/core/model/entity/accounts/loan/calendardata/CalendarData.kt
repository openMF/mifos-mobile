/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts.loan.calendardata

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class CalendarData(
    val id: Int? = null,

    val calendarInstanceId: Int? = null,

    val entityId: Int? = null,

    val entityType: EntityType,

    val title: String? = null,

    val startDate: List<Int> = emptyList(),

    val endDate: List<Int> = emptyList(),

    val duration: Double? = null,

    val type: Type,

    val repeating: Boolean? = null,

    val recurrence: String? = null,

    val frequency: Frequency,

    val interval: Double? = null,

    val repeatsOnNthDayOfMonth: RepeatsOnNthDayOfMonth,

    val firstReminder: Int? = null,

    val secondReminder: Int? = null,

    val humanReadable: String? = null,

    val createdDate: List<Int> = emptyList(),

    val lastUpdatedDate: List<Int> = emptyList(),

    val createdByUserId: Int? = null,

    val createdByUsername: String? = null,

    val lastUpdatedByUserId: Int? = null,

    val lastUpdatedByUsername: String? = null,
) : Parcelable
