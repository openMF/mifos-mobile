package org.mifos.mobilebanking.models.templates.account

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 10/03/17.
 */

@Parcelize
data class AccountType(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("code")
        var code: String? = null,

        @SerializedName("value")
        var value: String? = null
) : Parcelable