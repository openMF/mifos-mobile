package org.mifos.mobilebanking.models.accounts.share

import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import org.mifos.mobilebanking.models.accounts.Account
import org.mifos.mobilebanking.models.accounts.savings.Currency

@Parcelize
data class ShareAccount(

        @SerializedName("accountNo")
        @Expose
        var accountNo: String? = null,

        @SerializedName("totalApprovedShares")
        @Expose
        var totalApprovedShares: Int? = null,

        @SerializedName("totalPendingForApprovalShares")
        @Expose
        var totalPendingForApprovalShares: Int? = null,

        @SerializedName("productId")
        @Expose
        var productId: Int? = null,

        @SerializedName("productName")
        @Expose
        var productName: String? = null,

        @SerializedName("shortProductName")
        @Expose
        var shortProductName: String? = null,

        @SerializedName("status")
        @Expose
        var status: Status? = null,

        @SerializedName("currency")
        @Expose
        var currency: Currency? = null,

        @SerializedName("timeline")
        @Expose
        var timeline: Timeline? = null

) : Account(), Parcelable