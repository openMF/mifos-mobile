package org.mifos.mobile.core.model.entity.templates.loans

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Tarun on 12/16/2016.
 */

@Parcelize
data class AccountLinkingOptions(

    var accountNo: String? = null,

    var clientId: Int? = null,

    var clientName: String? = null,

    var currency: Currency? = null,

    var fieldOfficerId: Int? = null,

    var id: Int? = null,

    var productId: Int? = null,

    var productName: String? = null,
) : Parcelable
