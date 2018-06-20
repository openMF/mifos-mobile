package org.mifos.mobilebanking.models.accounts.savings

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 05/03/17.
 */

@Parcelize
data class PaymentDetailData(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("paymentType")
        var paymentType: PaymentType,

        @SerializedName("accountNumber")
        var accountNumber: String,

        @SerializedName("checkNumber")
        var checkNumber: String,

        @SerializedName("routingCode")
        var routingCode: String,

        @SerializedName("receiptNumber")
        var receiptNumber: String,

        @SerializedName("bankNumber")
        var bankNumber: String
) : Parcelable