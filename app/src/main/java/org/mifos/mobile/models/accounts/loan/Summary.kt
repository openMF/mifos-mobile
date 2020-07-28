package org.mifos.mobile.models.accounts.loan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Summary(
        var principalDisbursed: Double = 0.toDouble(),

        var principalPaid: Double = 0.toDouble(),

        var interestCharged: Double = 0.toDouble(),

        var interestPaid: Double = 0.toDouble(),

        var feeChargesCharged: Double = 0.toDouble(),

        var penaltyChargesCharged: Double = 0.toDouble(),

        var penaltyChargesWaived: Double = 0.toDouble(),

        var totalExpectedRepayment: Double = 0.toDouble(),

        var interestWaived: Double = 0.toDouble(),

        var totalRepayment: Double = 0.toDouble(),

        var feeChargesWaived: Double = 0.toDouble(),

        var totalOutstanding: Double = 0.toDouble(),

        private var overdueSinceDate: List<Int>? = null,

        var currency: Currency? = null
) : Parcelable {

    fun getOverdueSinceDate(): List<Int>? {
        return if (overdueSinceDate == null)
            null
        else
            overdueSinceDate
    }
}