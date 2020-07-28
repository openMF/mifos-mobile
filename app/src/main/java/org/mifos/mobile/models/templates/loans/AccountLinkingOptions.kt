package org.mifos.mobile.models.templates.loans

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Tarun on 12/16/2016.
 */

@Parcelize
data class AccountLinkingOptions(

        var accountNo: String,

        var clientId: Int? = null,

        var clientName: String,

        var currency: Currency,

        var fieldOfficerId: Int? = null,

        var id: Int? = null,

        var productId: Int? = null,

        var productName: String

) : Parcelable