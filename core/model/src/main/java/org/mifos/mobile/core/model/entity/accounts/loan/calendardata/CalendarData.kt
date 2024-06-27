package org.mifos.mobile.core.model.entity.accounts.loan.calendardata

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Rajan Maurya on 04/03/17.
 */

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
