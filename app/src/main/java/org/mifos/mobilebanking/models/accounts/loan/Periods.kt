package org.mifos.mobilebanking.models.accounts.loan

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

/**
 * Created by Rajan Maurya on 04/03/17.
 */

@Parcelize
data class Periods(
        @SerializedName("period")
        var period: Int? = null,

        @SerializedName("fromDate")
        var fromDate: List<Int> = ArrayList(),

        @SerializedName("dueDate")
        var dueDate: List<Int> = ArrayList(),

        @SerializedName("obligationsMetOnDate")
        var obligationsMetOnDate: List<Int> = ArrayList(),

        @SerializedName("principalDisbursed")
        var principalDisbursed: Double? = null,

        @SerializedName("complete")
        var complete: Boolean? = null,

        @SerializedName("daysInPeriod")
        var daysInPeriod: Int? = null,

        @SerializedName("principalOriginalDue")
        var principalOriginalDue: Double? = null,

        @SerializedName("principalDue")
        var principalDue: Double? = null,

        @SerializedName("principalPaid")
        var principalPaid: Double? = null,

        @SerializedName("principalWrittenOff")
        var principalWrittenOff: Double? = null,

        @SerializedName("principalOutstanding")
        var principalOutstanding: Double? = null,

        @SerializedName("principalLoanBalanceOutstanding")
        var principalLoanBalanceOutstanding: Double? = null,

        @SerializedName("interestOriginalDue")
        var interestOriginalDue: Double? = null,

        @SerializedName("interestDue")
        var interestDue: Double? = null,

        @SerializedName("interestPaid")
        var interestPaid: Double? = null,

        @SerializedName("interestWaived")
        var interestWaived: Double? = null,

        @SerializedName("interestWrittenOff")
        var interestWrittenOff: Double? = null,

        @SerializedName("interestOutstanding")
        var interestOutstanding: Double? = null,

        @SerializedName("feeChargesDue")
        var feeChargesDue: Double? = null,

        @SerializedName("feeChargesPaid")
        var feeChargesPaid: Double? = null,

        @SerializedName("feeChargesWaived")
        var feeChargesWaived: Double? = null,

        @SerializedName("feeChargesWrittenOff")
        var feeChargesWrittenOff: Double? = null,

        @SerializedName("feeChargesOutstanding")
        var feeChargesOutstanding: Double? = null,

        @SerializedName("penaltyChargesDue")
        var penaltyChargesDue: Double? = null,

        @SerializedName("penaltyChargesPaid")
        var penaltyChargesPaid: Double? = null,

        @SerializedName("penaltyChargesWaived")
        var penaltyChargesWaived: Double? = null,

        @SerializedName("penaltyChargesWrittenOff")
        var penaltyChargesWrittenOff: Double? = null,

        @SerializedName("penaltyChargesOutstanding")
        var penaltyChargesOutstanding: Double? = null,

        @SerializedName("totalOriginalDueForPeriod")
        var totalOriginalDueForPeriod: Double? = null,

        @SerializedName("totalDueForPeriod")
        var totalDueForPeriod: Double? = null,

        @SerializedName("totalPaidForPeriod")
        var totalPaidForPeriod: Double? = null,

        @SerializedName("totalPaidInAdvanceForPeriod")
        var totalPaidInAdvanceForPeriod: Double? = null,

        @SerializedName("totalPaidLateForPeriod")
        var totalPaidLateForPeriod: Double? = null,

        @SerializedName("totalWaivedForPeriod")
        var totalWaivedForPeriod: Double? = null,

        @SerializedName("totalWrittenOffForPeriod")
        var totalWrittenOffForPeriod: Double? = null,

        @SerializedName("totalOutstandingForPeriod")
        var totalOutstandingForPeriod: Double? = null,

        @SerializedName("totalOverdue")
        var totalOverdue: Double? = null,

        @SerializedName("totalActualCostOfLoanForPeriod")
        var totalActualCostOfLoanForPeriod: Double? = null,

        @SerializedName("totalInstallmentAmountForPeriod")
        var totalInstallmentAmountForPeriod: Double? = null

) : Parcelable