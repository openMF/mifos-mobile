package org.mifos.mobilebanking.models.accounts.share

import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Timeline(
        @SerializedName("submittedOnDate")
        @Expose
        var submittedOnDate: List<Int>? = null,

        @SerializedName("submittedByUsername")
        @Expose
        var submittedByUsername: String? = null,

        @SerializedName("submittedByFirstname")
        @Expose
        var submittedByFirstname: String? = null,

        @SerializedName("submittedByLastname")
        @Expose
        var submittedByLastname: String? = null,

        @SerializedName("approvedDate")
        @Expose
        var approvedDate: List<Int>? = null,

        @SerializedName("approvedByUsername")
        @Expose
        var approvedByUsername: String? = null,

        @SerializedName("approvedByFirstname")
        @Expose
        var approvedByFirstname: String? = null,

        @SerializedName("approvedByLastname")
        @Expose
        var approvedByLastname: String? = null,

        @SerializedName("activatedDate")
        @Expose
        var activatedDate: List<Int>? = null,

        @SerializedName("activatedByUsername")
        @Expose
        var activatedByUsername: String? = null,

        @SerializedName("activatedByFirstname")
        @Expose
        var activatedByFirstname: String? = null,

        @SerializedName("activatedByLastname")
        @Expose
        var activatedByLastname: String? = null

) : Parcelable