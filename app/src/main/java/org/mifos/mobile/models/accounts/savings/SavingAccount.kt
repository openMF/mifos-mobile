package org.mifos.mobile.models.accounts.savings

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import org.mifos.mobile.models.accounts.Account
import org.mifos.mobile.models.client.DepositType

/**
 * @author Vishwajeet
 * @since 22/06/16
 */

@Parcelize
data class SavingAccount(
        @SerializedName("accountNo")
        var accountNo: String? = null,

        @SerializedName("productName")
        var productName: String? = null,

        @SerializedName("productId")
        var productId: Int? = null,

        @SerializedName("overdraftLimit")
        var overdraftLimit: Long = 0,

        @SerializedName("minRequiredBalance")
        var minRequiredBalance: Long = 0,

        @SerializedName("accountBalance")
        var accountBalance: Double = 0.toDouble(),

        @SerializedName("totalDeposits")
        var totalDeposits: Double = 0.toDouble(),

        @SerializedName("savingsProductName")
        var savingsProductName: String? = null,

        @SerializedName("clientName")
        var clientName: String? = null,

        @SerializedName("savingsProductId")
        var savingsProductId: String? = null,

        @SerializedName("nominalAnnualInterestRate")
        var nominalAnnualInterestRate: Double = 0.toDouble(),

        @SerializedName("status")
        var status: Status? = null,

        @SerializedName("currency")
        var currency: Currency? = null,

        @SerializedName("depositType")
        var depositType: DepositType? = null,

        @SerializedName("lastActiveTransactionDate")
        var lastActiveTransactionDate: List<Int>? = null,

        @SerializedName("timeline")
        var timeLine: TimeLine? = null
) : Parcelable, Account() {
    fun isRecurring(): Boolean {
        return this.depositType != null && this.depositType!!.isRecurring()
    }
}