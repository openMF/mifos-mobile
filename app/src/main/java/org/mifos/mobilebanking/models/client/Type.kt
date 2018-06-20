package org.mifos.mobilebanking.models.client

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 23/02/17.
 */

@Parcelize
data class Type(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("code")
        var code: String,

        @SerializedName("value")
        var value: String
) : Parcelable