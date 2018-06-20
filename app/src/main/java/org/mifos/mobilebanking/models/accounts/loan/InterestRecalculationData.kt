package org.mifos.mobilebanking.models.accounts.loan

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import org.mifos.mobilebanking.models.accounts.loan.calendardata.CalendarData

/**
 * Created by Rajan Maurya on 04/03/17.
 */

@Parcelize
data class InterestRecalculationData(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("loanId")
        var loanId: Int? = null,

        @SerializedName("interestRecalculationCompoundingType")
        var interestRecalculationCompoundingType: InterestRecalculationCompoundingType,

        @SerializedName("rescheduleStrategyType")
        var rescheduleStrategyType: RescheduleStrategyType,

        @SerializedName("calendarData")
        var calendarData: CalendarData,

        @SerializedName("recalculationRestFrequencyType")
        var recalculationRestFrequencyType: RecalculationRestFrequencyType,

        @SerializedName("recalculationRestFrequencyInterval")
        var recalculationRestFrequencyInterval: Double? = null,

        @SerializedName("recalculationCompoundingFrequencyType")
        var recalculationCompoundingFrequencyType: RecalculationCompoundingFrequencyType,

        @SerializedName("isCompoundingToBePostedAsTransaction")
        var compoundingToBePostedAsTransaction: Boolean? = null,

        @SerializedName("allowCompoundingOnEod")
        var allowCompoundingOnEod: Boolean? = null

) : Parcelable