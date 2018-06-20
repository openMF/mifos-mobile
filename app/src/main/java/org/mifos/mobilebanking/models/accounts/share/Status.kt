package org.mifos.mobilebanking.models.accounts.share

import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Status(
        @SerializedName("id")
        @Expose
        var id: Int? = null,

        @SerializedName("code")
        @Expose
        var code: String? = null,

        @SerializedName("value")
        @Expose
        var value: String? = null,

        @SerializedName("submittedAndPendingApproval")
        @Expose
        var submittedAndPendingApproval: Boolean? = null,

        @SerializedName("approved")
        @Expose
        var approved: Boolean? = null,

        @SerializedName("rejected")
        @Expose
        var rejected: Boolean? = null,

        @SerializedName("active")
        @Expose
        var active: Boolean? = null,

        @SerializedName("closed")
        @Expose
        var closed: Boolean? = null

) : Parcelable