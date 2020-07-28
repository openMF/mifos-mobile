package org.mifos.mobile.models.accounts.loan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by Rajan Maurya on 04/03/17.
 */

@Parcelize
data class Periods(
        var period: Int? = null,

        var fromDate: List<Int> = ArrayList(),

        var dueDate: List<Int> = ArrayList(),

        var obligationsMetOnDate: List<Int> = ArrayList(),

        var principalDisbursed: Double? = null,

        var complete: Boolean? = null,

        var daysInPeriod: Int? = null,

        var principalOriginalDue: Double? = null,

        var principalDue: Double? = null,

        var principalPaid: Double? = null,

        var principalWrittenOff: Double? = null,

        var principalOutstanding: Double? = null,

        var principalLoanBalanceOutstanding: Double? = null,

        var interestOriginalDue: Double? = null,

        var interestDue: Double? = null,

        var interestPaid: Double? = null,

        var interestWaived: Double? = null,

        var interestWrittenOff: Double? = null,

        var interestOutstanding: Double? = null,

        var feeChargesDue: Double? = null,

        var feeChargesPaid: Double? = null,

        var feeChargesWaived: Double? = null,

        var feeChargesWrittenOff: Double? = null,

        var feeChargesOutstanding: Double? = null,

        var penaltyChargesDue: Double? = null,

        var penaltyChargesPaid: Double? = null,

        var penaltyChargesWaived: Double? = null,

        var penaltyChargesWrittenOff: Double? = null,

        var penaltyChargesOutstanding: Double? = null,

        var totalOriginalDueForPeriod: Double? = null,

        var totalDueForPeriod: Double? = null,

        var totalPaidForPeriod: Double? = null,

        var totalPaidInAdvanceForPeriod: Double? = null,

        var totalPaidLateForPeriod: Double? = null,

        var totalWaivedForPeriod: Double? = null,

        var totalWrittenOffForPeriod: Double? = null,

        var totalOutstandingForPeriod: Double? = null,

        var totalOverdue: Double? = null,

        var totalActualCostOfLoanForPeriod: Double? = null,

        var totalInstallmentAmountForPeriod: Double? = null

) : Parcelable