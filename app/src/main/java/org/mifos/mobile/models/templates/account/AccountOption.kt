package org.mifos.mobile.models.templates.account

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 10/03/17.
 */

@Parcelize
data class AccountOption(
        var accountId: Int? = null,

        var accountNo: String? = null,

        var accountType: AccountType? = null,

        var clientId: Long? = null,

        var clientName: String? = null,

        var officeId: Int? = null,

        var officeName: String? = null

) : Parcelable