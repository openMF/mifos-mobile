package org.mifos.mobile.core.model.entity.accounts.loan

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import org.mifos.mobile.core.model.entity.accounts.loan.calendardata.CalendarData

/**
 * Created by Rajan Maurya on 04/03/17.
 */

@Parcelize
data class InterestRecalculationData(
    var id: Int? = null,

    var loanId: Int? = null,

    var interestRecalculationCompoundingType: InterestRecalculationCompoundingType? = null,

    var rescheduleStrategyType: RescheduleStrategyType? = null,

    var calendarData: CalendarData,

    var recalculationRestFrequencyType: RecalculationRestFrequencyType? = null,

    var recalculationRestFrequencyInterval: Double? = null,

    var recalculationCompoundingFrequencyType: RecalculationCompoundingFrequencyType? = null,

    @SerializedName("isCompoundingToBePostedAsTransaction")
    var compoundingToBePostedAsTransaction: Boolean? = null,

    var allowCompoundingOnEod: Boolean? = null,

    ) : Parcelable
