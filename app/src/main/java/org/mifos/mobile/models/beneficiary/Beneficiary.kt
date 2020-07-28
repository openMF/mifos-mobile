package org.mifos.mobile.models.beneficiary

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.mifos.mobile.models.templates.account.AccountType

/**
 * Created by dilpreet on 14/6/17.
 */

@Parcelize
data class Beneficiary(
        var id: Int? = null,

        var name: String? = null,

        var officeName: String? = null,

        var clientName: String? = null,

        var accountType: AccountType? = null,

        var accountNumber: String? = null,

        var transferLimit: Double? = null

) : Parcelable