package org.mifos.mobile.models.accounts.loan

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 04/03/17.
 */

@Parcelize
data class InterestType(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("code")
        var code: String,

        @SerializedName("value")
        var value: String
) : Parcelable