package org.mifos.mobilebanking.models.accounts.loan.calendardata

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

/**
 * Created by Rajan Maurya on 04/03/17.
 */

@Parcelize
data class CalendarData(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("calendarInstanceId")
        var calendarInstanceId: Int? = null,

        @SerializedName("entityId")
        var entityId: Int? = null,

        @SerializedName("entityType")
        var entityType: EntityType,

        @SerializedName("title")
        var title: String,

        @SerializedName("startDate")
        var startDate: List<Int> = ArrayList(),

        @SerializedName("endDate")
        var endDate: List<Int> = ArrayList(),

        @SerializedName("duration")
        var duration: Double? = null,

        @SerializedName("type")
        var type: Type,

        @SerializedName("repeating")
        var repeating: Boolean? = null,

        @SerializedName("recurrence")
        var recurrence: String,

        @SerializedName("frequency")
        var frequency: Frequency,

        @SerializedName("interval")
        var interval: Double? = null,

        @SerializedName("repeatsOnNthDayOfMonth")
        var repeatsOnNthDayOfMonth: RepeatsOnNthDayOfMonth,

        @SerializedName("firstReminder")
        var firstReminder: Int? = null,

        @SerializedName("secondReminder")
        var secondReminder: Int? = null,

        @SerializedName("humanReadable")
        var humanReadable: String,

        @SerializedName("createdDate")
        var createdDate: List<Int> = ArrayList(),

        @SerializedName("lastUpdatedDate")
        var lastUpdatedDate: List<Int> = ArrayList(),

        @SerializedName("createdByUserId")
        var createdByUserId: Int? = null,

        @SerializedName("createdByUsername")
        var createdByUsername: String,

        @SerializedName("lastUpdatedByUserId")
        var lastUpdatedByUserId: Int? = null,

        @SerializedName("lastUpdatedByUsername")
        var lastUpdatedByUsername: String

) : Parcelable