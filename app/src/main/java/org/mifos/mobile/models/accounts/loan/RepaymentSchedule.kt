package org.mifos.mobile.models.accounts.loan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by Rajan Maurya on 04/03/17.
 */

@Parcelize
data class RepaymentSchedule(
        var currency: Currency,

        var loanTermInDays: Int? = null,

        var totalPrincipalDisbursed: Double? = null,

        var totalPrincipalExpected: Double? = null,

        var totalPrincipalPaid: Double? = null,

        var totalInterestCharged: Double? = null,

        var totalFeeChargesCharged: Double? = null,

        var totalPenaltyChargesCharged: Double? = null,

        var totalWaived: Double? = null,

        var totalWrittenOff: Double? = null,

        var totalRepaymentExpected: Double? = null,

        var totalRepayment: Double? = null,

        var totalPaidInAdvance: Double? = null,

        var totalPaidLate: Double? = null,

        var totalOutstanding: Double? = null,

        var periods: List<Periods> = ArrayList()

) : Parcelable