package org.mifos.mobile.models.paymentHub

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Amount(
        @SerializedName("currency")
        var currency: String? = null,

        @SerializedName("amount")
        var amount: String? = null

) : Parcelable