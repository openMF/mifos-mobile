package org.mifos.mobilebanking.models.accounts.savings

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by Rajan Maurya on 05/03/17.
 */

@Parcelize
data class Summary(
        @SerializedName("currency")
        var currency: Currency,

        @SerializedName("totalDeposits")
        var totalDeposits: Double? = null,

        @SerializedName("totalWithdrawals")
        var totalWithdrawals: Double? = null,

        @SerializedName("totalInterestEarned")
        var totalInterestEarned: Double? = null,

        @SerializedName("totalInterestPosted")
        var totalInterestPosted: Double? = null,

        @SerializedName("accountBalance")
        var accountBalance: Double? = null,

        @SerializedName("totalOverdraftInterestDerived")
        var totalOverdraftInterestDerived: Double? = null,

        @SerializedName("interestNotPosted")
        var interestNotPosted: Double? = null,

        @SerializedName("lastInterestCalculationDate")
        var lastInterestCalculationDate: List<Int>

) : Parcelable