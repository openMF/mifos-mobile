package org.mifos.mobile.models.accounts.loan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Status(
        var id: Int? = null,

        var code: String? = null,

        var value: String? = null,

        var pendingApproval: Boolean? = null,

        var waitingForDisbursal: Boolean? = null,

        var active: Boolean? = null,

        var closedObligationsMet: Boolean? = null,

        var closedWrittenOff: Boolean? = null,

        var closedRescheduled: Boolean? = null,

        var closed: Boolean? = null,

        var overpaid: Boolean? = null

) : Parcelable {

    fun isLoanTypeWithdrawn(): Boolean {
        return !(this.active == true || this.closed == true || this.pendingApproval == true
                || this.waitingForDisbursal == true || this.closedObligationsMet == true
                || this.closedWrittenOff == true || this.closedRescheduled == true
                || this.overpaid == true)
    }
}