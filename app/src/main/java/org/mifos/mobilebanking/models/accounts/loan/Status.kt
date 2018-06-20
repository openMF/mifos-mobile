package org.mifos.mobilebanking.models.accounts.loan

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Status(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("code")
        var code: String? = null,

        @SerializedName("value")
        var value: String? = null,

        @SerializedName("pendingApproval")
        var pendingApproval: Boolean? = null,

        @SerializedName("waitingForDisbursal")
        var waitingForDisbursal: Boolean? = null,

        @SerializedName("active")
        var active: Boolean? = null,

        @SerializedName("closedObligationsMet")
        var closedObligationsMet: Boolean? = null,

        @SerializedName("closedWrittenOff")
        var closedWrittenOff: Boolean? = null,

        @SerializedName("closedRescheduled")
        var closedRescheduled: Boolean? = null,

        @SerializedName("closed")
        var closed: Boolean? = null,

        @SerializedName("overpaid")
        var overpaid: Boolean? = null

) : Parcelable {

    fun isLoanTypeWithdrawn(): Boolean {
        return !(this.active!! || this.closed!! || this.pendingApproval!!
                || this.waitingForDisbursal!! || this.closedObligationsMet!!
                || this.closedWrittenOff!! || this.closedRescheduled!!
                || this.overpaid!!)
    }
}