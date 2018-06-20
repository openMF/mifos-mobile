package org.mifos.mobilebanking.models.templates.loans

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class Currency(
        @SerializedName("code")
        var code: String,

        @SerializedName("name")
        var name: String,

        @SerializedName("decimalPlaces")
        var decimalPlaces: Double? = null,

        @SerializedName("inMultiplesOf")
        var inMultiplesOf: Int? = null,

        @SerializedName("displaySymbol")
        var displaySymbol: String,

        @SerializedName("nameCode")
        var nameCode: String,

        @SerializedName("displayLabel")
        var displayLabel: String
) : Parcelable