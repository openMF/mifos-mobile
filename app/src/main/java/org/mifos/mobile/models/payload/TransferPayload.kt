package org.mifos.mobile.models.payload

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 10/03/17.
 */

@Parcelize
data class TransferPayload(
        var fromOfficeId: Int? = null,

        var fromClientId: Long? = null,

        var fromAccountType: Int? = null,

        var fromAccountId: Int? = null,

        var toOfficeId: Int? = null,

        var toClientId: Long? = null,

        var toAccountType: Int? = null,

        var toAccountId: Int? = null,

        var transferDate: String? = null,

        var transferAmount: Double? = null,

        var transferDescription: String? = null,

        var dateFormat : String = "dd MMMM yyyy",

        var locale : String = "en",

        @Transient
        var fromAccountNumber: String? = null,

        @Transient
        var toAccountNumber: String? = null

) : Parcelable