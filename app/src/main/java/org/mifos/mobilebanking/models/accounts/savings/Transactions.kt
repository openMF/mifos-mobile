package org.mifos.mobilebanking.models.accounts.savings

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

/**
 * Created by Rajan Maurya on 05/03/17.
 */

@Parcelize
data class Transactions(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("transactionType")
        var transactionType: TransactionType,

        @SerializedName("accountId")
        var accountId: Int? = null,

        @SerializedName("accountNo")
        var accountNo: String,

        @SerializedName("date")
        var date: List<Int> = ArrayList(),

        @SerializedName("currency")
        var currency: Currency,

        @SerializedName("paymentDetailData")
        var paymentDetailData: PaymentDetailData,

        @SerializedName("amount")
        var amount: Double? = null,

        @SerializedName("runningBalance")
        var runningBalance: Double? = null,

        @SerializedName("reversed")
        var reversed: Boolean? = null,

        @SerializedName("submittedOnDate")
        var submittedOnDate: List<Int>,

        @SerializedName("interestedPostedAsOn")
        var interestedPostedAsOn: Boolean? = null

) : Parcelable