package org.mifos.mobilebanking.models.accounts.loan

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.Locale

@Parcelize
data class Summary(
        @SerializedName("principalDisbursed")
        var principalDisbursed: Double = 0.toDouble(),

        @SerializedName("principalPaid")
        var principalPaid: Double = 0.toDouble(),

        @SerializedName("interestCharged")
        var interestCharged: Double = 0.toDouble(),

        @SerializedName("interestPaid")
        var interestPaid: Double = 0.toDouble(),

        @SerializedName("feeChargesCharged")
        var feeChargesCharged: Double = 0.toDouble(),

        @SerializedName("penaltyChargesCharged")
        var penaltyChargesCharged: Double = 0.toDouble(),

        @SerializedName("penaltyChargesWaived")
        var penaltyChargesWaived: Double = 0.toDouble(),

        @SerializedName("totalExpectedRepayment")
        var totalExpectedRepayment: Double = 0.toDouble(),

        @SerializedName("interestWaived")
        var interestWaived: Double = 0.toDouble(),

        @SerializedName("totalRepayment")
        var totalRepayment: Double = 0.toDouble(),

        @SerializedName("feeChargesWaived")
        var feeChargesWaived: Double = 0.toDouble(),

        @SerializedName("totalOutstanding")
        var totalOutstanding: Double = 0.toDouble(),

        @SerializedName("overdueSinceDate")
        private var overdueSinceDate: List<Int>? = null,

        @SerializedName("currency")
        var currency: Currency? = null
) : Parcelable {

    fun getOverdueSinceDate(): List<Int>? {
        if (overdueSinceDate==null)
            return null
        else
            return overdueSinceDate
    }
}