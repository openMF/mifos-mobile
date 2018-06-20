package org.mifos.mobilebanking.models.accounts.loan

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

/**
 * Created by Rajan Maurya on 04/03/17.
 */

@Parcelize
data class RepaymentSchedule(
        @SerializedName("currency")
        var currency: Currency,

        @SerializedName("loanTermInDays")
        var loanTermInDays: Int? = null,

        @SerializedName("totalPrincipalDisbursed")
        var totalPrincipalDisbursed: Double? = null,

        @SerializedName("totalPrincipalExpected")
        var totalPrincipalExpected: Double? = null,

        @SerializedName("totalPrincipalPaid")
        var totalPrincipalPaid: Double? = null,

        @SerializedName("totalInterestCharged")
        var totalInterestCharged: Double? = null,

        @SerializedName("totalFeeChargesCharged")
        var totalFeeChargesCharged: Double? = null,

        @SerializedName("totalPenaltyChargesCharged")
        var totalPenaltyChargesCharged: Double? = null,

        @SerializedName("totalWaived")
        var totalWaived: Double? = null,

        @SerializedName("totalWrittenOff")
        var totalWrittenOff: Double? = null,

        @SerializedName("totalRepaymentExpected")
        var totalRepaymentExpected: Double? = null,

        @SerializedName("totalRepayment")
        var totalRepayment: Double? = null,

        @SerializedName("totalPaidInAdvance")
        var totalPaidInAdvance: Double? = null,

        @SerializedName("totalPaidLate")
        var totalPaidLate: Double? = null,

        @SerializedName("totalOutstanding")
        var totalOutstanding: Double? = null,

        @SerializedName("periods")
        var periods: List<Periods> = ArrayList()

) : Parcelable