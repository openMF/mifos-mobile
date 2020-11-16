package org.mifos.mobile.models.accounts.share

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Timeline(
        @Expose
        var submittedOnDate: List<Int>? = null,

        @Expose
        var submittedByUsername: String? = null,

        @Expose
        var submittedByFirstname: String? = null,

        @Expose
        var submittedByLastname: String? = null,

        @Expose
        var approvedDate: List<Int>? = null,

        @Expose
        var approvedByUsername: String? = null,

        @Expose
        var approvedByFirstname: String? = null,

        @Expose
        var approvedByLastname: String? = null,

        @Expose
        var activatedDate: List<Int>? = null,

        @Expose
        var activatedByUsername: String? = null,

        @Expose
        var activatedByFirstname: String? = null,

        @Expose
        var activatedByLastname: String? = null

) : Parcelable