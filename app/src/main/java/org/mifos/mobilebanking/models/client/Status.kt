package org.mifos.mobilebanking.models.client

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 22/10/16.
 */

@Parcelize
data class Status(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("code")
        var code: String? = null,

        @SerializedName("value")
        var value: String? = null
) : Parcelable