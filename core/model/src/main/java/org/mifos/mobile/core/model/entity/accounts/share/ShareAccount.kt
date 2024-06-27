package org.mifos.mobile.core.model.entity.accounts.share

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize
import org.mifos.mobile.core.model.entity.accounts.Account
import org.mifos.mobile.core.model.entity.accounts.savings.Currency


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
    var timeline: Timeline? = null,

    ) : Account(), Parcelable
