package org.mifos.mobile.models.accounts.loan

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import org.mifos.mobile.models.accounts.loan.calendardata.CalendarData

/**
 * Created by Rajan Maurya on 04/03/17.
 */

@Parcelize
data class InterestRecalculationData(
        var id: Int? = null,

        var loanId: Int? = null,

        var interestRecalculationCompoundingType: InterestRecalculationCompoundingType,

        var rescheduleStrategyType: RescheduleStrategyType,

        var calendarData: CalendarData,

        var recalculationRestFrequencyType: RecalculationRestFrequencyType,

        var recalculationRestFrequencyInterval: Double? = null,

        var recalculationCompoundingFrequencyType: RecalculationCompoundingFrequencyType,

        @SerializedName("isCompoundingToBePostedAsTransaction")
        var compoundingToBePostedAsTransaction: Boolean? = null,

        var allowCompoundingOnEod: Boolean? = null

) : Parcelable