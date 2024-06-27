package org.mifos.mobile.core.model.entity.accounts.savings

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Status(
    var id: Int? = null,
    var code: String? = null,

    var value: String? = null,

    var submittedAndPendingApproval: Boolean? = null,

    var approved: Boolean? = null,

    var rejected: Boolean? = null,

    var withdrawnByApplicant: Boolean? = null,

    var active: Boolean? = null,

    var closed: Boolean? = null,

    var prematureClosed: Boolean? = null,

    internal var transferInProgress: Boolean? = null,

    internal var transferOnHold: Boolean? = null,

    var matured: Boolean? = null,

) : Parcelable
