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

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalendarData(
    var id: Int? = null,

    var calendarInstanceId: Int? = null,

    var entityId: Int? = null,

    var entityType: EntityType,

    var title: String? = null,

    var startDate: List<Int> = ArrayList(),

    var endDate: List<Int> = ArrayList(),

    var duration: Double? = null,

    var type: Type,

    var repeating: Boolean? = null,

    var recurrence: String? = null,

    var frequency: Frequency,

    var interval: Double? = null,

    var repeatsOnNthDayOfMonth: RepeatsOnNthDayOfMonth,

    var firstReminder: Int? = null,

    var secondReminder: Int? = null,

    var humanReadable: String? = null,

    var createdDate: List<Int> = ArrayList(),

    var lastUpdatedDate: List<Int> = ArrayList(),

    var createdByUserId: Int? = null,

    var createdByUsername: String? = null,

    var lastUpdatedByUserId: Int? = null,

    var lastUpdatedByUsername: String? = null,
) : Parcelable
