package org.mifos.mobilebanking.models.accounts.loan

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by dilpreet on 27/2/17.
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
        var inMultiplesOf: Double? = null,

        @SerializedName("displaySymbol")
        var displaySymbol: String,

        @SerializedName("nameCode")
        var nameCode: String,

        @SerializedName("displayLabel")
        var displayLabel: String

) : Parcelable