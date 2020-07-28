package org.mifos.mobile.models.accounts.share

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize
import org.mifos.mobile.models.accounts.Account
import org.mifos.mobile.models.accounts.savings.Currency

@Parcelize
data class ShareAccount(

        @Expose
        var accountNo: String? = null,

        @Expose
        var totalApprovedShares: Int? = null,

        @Expose
        var totalPendingForApprovalShares: Int? = null,

        @Expose
        var productId: Int? = null,

        @Expose
        var productName: String? = null,

        @Expose
        var shortProductName: String? = null,

        @Expose
        var status: Status? = null,

        @Expose
        var currency: Currency? = null,

        @Expose
        var timeline: Timeline? = null

) : Account(), Parcelable