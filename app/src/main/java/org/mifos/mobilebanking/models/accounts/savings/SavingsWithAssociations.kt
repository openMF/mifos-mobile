package org.mifos.mobilebanking.models.accounts.savings

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import org.mifos.mobilebanking.models.client.DepositType

import java.util.ArrayList

/**
 * @author Vishwajeet
 * @since 22/06/16
 */

@Parcelize
data class SavingsWithAssociations(
        @SerializedName("id")
        var id: Long? = null,

        @SerializedName("accountNo")
        var accountNo: String,

        @SerializedName("depositType")
        var depositType: DepositType? = null,

        @SerializedName("externalId")
        var externalId: String,

        @SerializedName("clientId")
        var clientId: Int? = null,

        @SerializedName("clientName")
        var clientName: String,

        @SerializedName("savingsProductId")
        var savingsProductId: Int? = null,

        @SerializedName("savingsProductName")
        var savingsProductName: String,

        @SerializedName("fieldOfficerId")
        var fieldOfficerId: Int? = null,

        @SerializedName("status")
        var status: Status,

        @SerializedName("timeline")
        var timeline: TimeLine,

        @SerializedName("currency")
        var currency: Currency,

        @SerializedName("nominalAnnualInterestRate")
        internal var nominalAnnualInterestRate: Double? = null,

        @SerializedName("minRequiredOpeningBalance")
        var minRequiredOpeningBalance: Double? = null,

        @SerializedName("lockinPeriodFrequency")
        var lockinPeriodFrequency: Double? = null,

        @SerializedName("withdrawalFeeForTransfers")
        var withdrawalFeeForTransfers: Boolean? = null,

        @SerializedName("allowOverdraft")
        var allowOverdraft: Boolean? = null,

        @SerializedName("enforceMinRequiredBalance")
        var enforceMinRequiredBalance: Boolean? = null,

        @SerializedName("withHoldTax")
        var withHoldTax: Boolean? = null,

        @SerializedName("lastActiveTransactionDate")
        var lastActiveTransactionDate: List<Int>,

        @SerializedName("isDormancyTrackingActive")
        var dormancyTrackingActive: Boolean? = null,

        @SerializedName("summary")
        var summary: Summary,

        @SerializedName("transactions")
        var transactions: List<Transactions> = ArrayList()

) : Parcelable {

    fun isRecurring() : Boolean {
        return this.depositType != null && this.depositType!!.isRecurring()
    }

    fun setNominalAnnualInterestRate(nominalAnnualInterestRate: Double?) {
        this.nominalAnnualInterestRate = nominalAnnualInterestRate
    }

    fun getNominalAnnualInterestRate(): Double {
        return nominalAnnualInterestRate!!
    }

    fun setNominalAnnualInterestRate(nominalAnnualInterestRate: Double) {
        this.nominalAnnualInterestRate = nominalAnnualInterestRate
    }


}