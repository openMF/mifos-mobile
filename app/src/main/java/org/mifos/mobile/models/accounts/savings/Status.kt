package org.mifos.mobile.models.accounts.savings

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Status(
        var id: Int? = null,
        var code: String,

        var value: String,

        var submittedAndPendingApproval: Boolean? = null,

        var approved: Boolean? = null,

        var rejected: Boolean? = null,

        var withdrawnByApplicant: Boolean? = null,

        var active: Boolean? = null,

        var closed: Boolean? = null,

        var prematureClosed: Boolean? = null,

        internal var transferInProgress: Boolean? = null,

        internal var transferOnHold: Boolean? = null,

        var matured: Boolean? = null

) : Parcelable