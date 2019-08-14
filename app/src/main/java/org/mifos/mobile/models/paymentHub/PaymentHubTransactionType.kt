package org.mifos.mobile.models.paymentHub

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentHubTransactionType(
        @SerializedName("scenario")
        var scenario: String? = null,

        @SerializedName("initiator")
        var initiator: String? = null,

        @SerializedName("initiatorType")
        var initiatorType: String? = null

) : Parcelable