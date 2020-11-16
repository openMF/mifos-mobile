package org.mifos.mobile.models.templates.account

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 10/03/17.
 */

@Parcelize
data class AccountType(

        var id: Int? = null,

        var code: String? = null,

        var value: String? = null
) : Parcelable