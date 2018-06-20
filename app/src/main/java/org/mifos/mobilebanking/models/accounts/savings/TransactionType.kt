package org.mifos.mobilebanking.models.accounts.savings

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 05/03/17.
 */

@Parcelize
data class TransactionType(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("code")
        var code: String,

        @SerializedName("value")
        var value: String,

        @SerializedName("deposit")
        var deposit: Boolean? = null,

        @SerializedName("dividendPayout")
        var dividendPayout: Boolean? = null,

        @SerializedName("withdrawal")
        var withdrawal: Boolean? = null,

        @SerializedName("interestPosting")
        var interestPosting: Boolean? = null,

        @SerializedName("feeDeduction")
        var feeDeduction: Boolean? = null,

        @SerializedName("initiateTransfer")
        var initiateTransfer: Boolean? = null,

        @SerializedName("approveTransfer")
        var approveTransfer: Boolean? = null,

        @SerializedName("withdrawTransfer")
        var withdrawTransfer: Boolean? = null,

        @SerializedName("rejectTransfer")
        var rejectTransfer: Boolean? = null,

        @SerializedName("overdraftInterest")
        var overdraftInterest: Boolean? = null,

        @SerializedName("writtenoff")
        var writtenoff: Boolean? = null,

        @SerializedName("overdraftFee")
        var overdraftFee: Boolean? = null,

        @SerializedName("withholdTax")
        var withholdTax: Boolean? = null,

        @SerializedName("escheat")
        var escheat: Boolean? = null

) : Parcelable