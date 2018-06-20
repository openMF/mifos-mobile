package org.mifos.mobilebanking.models.accounts.savings

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Status(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("code")
        var code: String,

        @SerializedName("value")
        var value: String,

        @SerializedName("submittedAndPendingApproval")
        var submittedAndPendingApproval: Boolean? = null,

        @SerializedName("approved")
        var approved: Boolean? = null,

        @SerializedName("rejected")
        var rejected: Boolean? = null,

        @SerializedName("withdrawnByApplicant")
        var withdrawnByApplicant: Boolean? = null,

        @SerializedName("active")
        var active: Boolean? = null,

        @SerializedName("closed")
        var closed: Boolean? = null,

        @SerializedName("prematureClosed")
        var prematureClosed: Boolean? = null,

        @SerializedName("transferInProgress")
        internal var transferInProgress: Boolean? = null,

        @SerializedName("transferOnHold")
        internal var transferOnHold: Boolean? = null,

        @SerializedName("matured")
        var matured: Boolean? = null

) : Parcelable