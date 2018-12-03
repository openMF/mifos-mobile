package org.mifos.mobilebanking.models.beneficiary

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by dilpreet on 16/6/17.
 */

@Parcelize
data class BeneficiaryPayload(
        internal var locale : String = "en_GB",

        @SerializedName("name")
        var name: String? = null,

        @SerializedName("accountNumber")
        var accountNumber: String? = null,

        @SerializedName("accountType")
        var accountType: Int = 0,

        @SerializedName("transferLimit")
        var transferLimit: Float = 0f,

        @SerializedName("officeName")
        var officeName: String? = null
) : Parcelable