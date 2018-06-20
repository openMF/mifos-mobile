package org.mifos.mobilebanking.models.templates.loans

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Tarun on 12/16/2016.
 */

@Parcelize
data class AccountLinkingOptions(
        @SerializedName("accountNo")
        var accountNo: String,

        @SerializedName("clientId")
        var clientId: Int? = null,

        @SerializedName("clientName")
        var clientName: String,

        @SerializedName("currency")
        var currency: Currency,

        @SerializedName("fieldOfficerId")
        var fieldOfficerId: Int? = null,

        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("productId")
        var productId: Int? = null,

        @SerializedName("productName")
        var productName: String

) : Parcelable