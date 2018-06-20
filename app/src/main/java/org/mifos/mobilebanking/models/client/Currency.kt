package org.mifos.mobilebanking.models.client

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Currency(
        @SerializedName("code")
        var code: String,

        @SerializedName("name")
        var name: String,

        @SerializedName("decimalPlaces")
        var decimalPlaces: Int? = null,

        @SerializedName("displaySymbol")
        var displaySymbol: String? = null,

        @SerializedName("nameCode")
        var nameCode: String,

        @SerializedName("displayLabel")
        var displayLabel: String
) : Parcelable